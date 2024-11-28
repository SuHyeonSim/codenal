package com.codenal.annual.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.repository.AnnualLeaveManageRepository;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.domain.ApprovalDto;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class AnnualLeaveManageService {

    private static final Logger logger = LoggerFactory.getLogger(AnnualLeaveManageService.class);

    private final AnnualLeaveManageRepository annualLeaveManageRepository;
    private final EmployeeRepository employeeRepository;
    private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
    private final RestTemplate restTemplate;

    @Value("${approval.api.url}")
    private String approvalApiUrl;

    @Autowired
    public AnnualLeaveManageService(
            RestTemplate restTemplate,
            AnnualLeaveManageRepository annualLeaveManageRepository,
            EmployeeRepository employeeRepository,
            AnnualLeaveUsageRepository annualLeaveUsageRepository) {
        this.restTemplate = restTemplate;
        this.annualLeaveManageRepository = annualLeaveManageRepository;
        this.employeeRepository = employeeRepository;
        this.annualLeaveUsageRepository = annualLeaveUsageRepository;
    }

    public AnnualLeaveManage getAnnualLeaveManageById(Long empId) {
        return annualLeaveManageRepository.findByEmployee_EmpId(empId);
    }

    // 연차 발생 및 소멸을 처리하는 메서드
    @Transactional
    public void updateAnnualLeaveBalances() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            updateAnnualLeaveForEmployee(employee);
        }
    }

    @Transactional
    public void updateAnnualLeaveForEmployee(Employee employee) {
        Long empId = employee.getEmpId();
        AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(empId);

        if (manage == null) {
            // 신규 직원인 경우 AnnualLeaveManage 생성
            manage = new AnnualLeaveManage();
            manage.setEmployee(employee);
            manage.setAnnualUsedDay(0.0);
            manage.setAnnualTotalDay((double) calculateTotalAnnualLeave(employee));
            manage.setAnnualRemainDay(manage.getAnnualTotalDay());
            annualLeaveManageRepository.save(manage);
        } else {
            // 이미 존재하는 경우 업데이트
            updateAnnualLeaveBalancesForEmployee(employee, manage);
        }
    }

    @Transactional
    public void updateAnnualLeaveBalancesForEmployee(Employee employee, AnnualLeaveManage manage) {
        // 총 연차 계산
        int totalAnnualLeave = calculateTotalAnnualLeave(employee);

        // 사용 연차 및 잔여 연차 계산
        double usedAnnualLeave = manage.getAnnualUsedDay();
        double remainingAnnualLeave = totalAnnualLeave - usedAnnualLeave;

        // 잔여 연차가 음수가 되지 않도록 조정
        remainingAnnualLeave = Math.max(remainingAnnualLeave, 0.0);

        // 업데이트
        manage.setAnnualTotalDay((double) totalAnnualLeave);
        manage.setAnnualRemainDay(remainingAnnualLeave);

        annualLeaveManageRepository.save(manage);
    }

    private int calculateTotalAnnualLeave(Employee employee) {
        LocalDate hireDate = employee.getEmpHire();
        LocalDate today = LocalDate.now();

        // 완료된 월 수 계산
        long monthsBetween = ChronoUnit.MONTHS.between(hireDate, today);
        if (today.getDayOfMonth() < hireDate.getDayOfMonth()) {
            monthsBetween -= 1;
        }
        monthsBetween = Math.max(monthsBetween, 0); // 음수 방지

        if (monthsBetween < 12) {
            // 첫 1년 동안 매월 1일씩 연차 발생 (최대 11일)
            return (int) Math.min(monthsBetween, 11);
        } else {
            // 1년 근무 시 연차 15일 발생
            int totalAnnualLeave = 15;

            // 이후 매 2년마다 1일씩 추가 연차 발생
            long additionalYears = (monthsBetween - 12) / 24;
            totalAnnualLeave += additionalYears;

            return totalAnnualLeave;
        }
    }

    @Transactional
    public void resetAnnualLeaveIfAnniversary() {
        List<Employee> employees = employeeRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Employee employee : employees) {
            LocalDate hireDate = employee.getEmpHire();
            if (hireDate != null && hireDate.getMonthValue() == today.getMonthValue() && hireDate.getDayOfMonth() == today.getDayOfMonth()) {
                AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(employee.getEmpId());
                if (manage != null) {
                    // 연차 소멸: 사용하지 않은 연차를 0으로 설정하고 새로운 연차를 부여
                    // 사용 연차는 유지하고, 잔여 연차만 초기화 또는 재설정
                    // 비즈니스 로직에 따라 수정 필요
                    // 예시: 사용 연차는 유지하고, 총 연차를 재계산하여 잔여 연차 설정
                    int totalAnnualLeave = calculateTotalAnnualLeave(employee);
                    double usedAnnualLeave = manage.getAnnualUsedDay();
                    double remainingAnnualLeave = totalAnnualLeave - usedAnnualLeave;
                    remainingAnnualLeave = Math.max(remainingAnnualLeave, 0.0);

                    manage.setAnnualTotalDay((double) totalAnnualLeave);
                    manage.setAnnualRemainDay(remainingAnnualLeave);

                    annualLeaveManageRepository.save(manage);
                }
            }
        }
    }

    // 매일 자정에 연차 정보를 업데이트
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledUpdateAnnualLeaveBalances() {
        updateAnnualLeaveBalances();
    }

    // 매일 자정에 연차 소멸 처리
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledResetAnnualLeaveIfAnniversary() {
        resetAnnualLeaveIfAnniversary();
    }

    public void save(AnnualLeaveManage manage) {
        annualLeaveManageRepository.save(manage);
    }

    @Scheduled(fixedDelay = 300000) // 5분마다 실행
    public void processApprovedAnnualLeaves() {
        String url = approvalApiUrl; // Approval 시스템의 API URL

        try {
            // HTTP 요청에 Accept 헤더 추가: JSON 형식을 기대한다는 것을 명시
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<ApprovalDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ApprovalDto>>() {}
            );

            List<ApprovalDto> approvals = response.getBody();
            if (approvals != null) {
                for (ApprovalDto approvalDto : approvals) {
                    // 연차 사용 내역 가져오기
                    Optional<AnnualLeaveUsage> optionalAlu = annualLeaveUsageRepository.findById(approvalDto.getAnnual_leave_usage_no());
                    if (optionalAlu.isPresent()) {
                        AnnualLeaveUsage alu = optionalAlu.get();
                        if (!alu.isProcessed()) { // 처리 여부 확인
                            updateAnnualLeaveUsage(alu.getEmployee().getEmpId(), alu.getTotalDay());
                            alu.setProcessed(true); // 처리 완료 표시
                            annualLeaveUsageRepository.save(alu);
                        }
                    }
                }
            }
        } catch (HttpClientErrorException e) {
            logger.error("API 요청 중 클라이언트 오류 발생: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            logger.error("API 요청 중 서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            logger.error("연차 사용 승인 처리 중 오류 발생", e);
        }
    }

    @Transactional
    public void updateAnnualLeaveUsage(Long empId, double usedDay) {
        AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(empId);

        if (manage != null) {
            manage.setAnnualUsedDay(manage.getAnnualUsedDay() + usedDay);
            manage.setAnnualRemainDay(manage.getAnnualRemainDay() - usedDay);
            // 잔여 연차가 음수가 되지 않도록 처리
            if (manage.getAnnualRemainDay() < 0) {
                manage.setAnnualRemainDay(0.0);
            }
            annualLeaveManageRepository.save(manage);
        } else {
            // 연차 관리 정보가 없을 경우 생성
            Employee employee = employeeRepository.findByEmpId(empId);
            if (employee != null) {
                manage = new AnnualLeaveManage();
                manage.setEmployee(employee);
                manage.setAnnualTotalDay(usedDay);
                manage.setAnnualUsedDay(usedDay);
                manage.setAnnualRemainDay(0.0);
                annualLeaveManageRepository.save(manage);
            } else {
                throw new IllegalArgumentException("존재하지 않는 직원 ID입니다: " + empId);
            }
        }
    }
    public AnnualLeaveManage getOrCreateAnnualLeaveManageById(Long empId) {
        AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(empId);
        if (manage == null) {
            Employee employee = employeeRepository.findByEmpId(empId);
            if (employee == null) {
                throw new IllegalArgumentException("Invalid empId: " + empId);
            }
            manage = AnnualLeaveManage.builder()
                    .employee(employee)
                    .annualTotalDay((double) calculateTotalAnnualLeave(employee))
                    .annualUsedDay(0.0)
                    .annualRemainDay((double) calculateTotalAnnualLeave(employee))
                    .build();
            annualLeaveManageRepository.save(manage);
        }
        return manage;
    }
    
}
