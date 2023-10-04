package com.example.preordering.service;

import com.example.preordering.constants.OrderStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.*;
import com.example.preordering.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserAdminRepository userAdminRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;
    private final CompanyRepository companyRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
//    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserAdminSettingOfTimetableRepository userAdminSettingOfTimetableRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserAdminService.class);


    public void deleteUser(String username){
       UserAdmin userAdmin =
               userAdminRepository.findByUsername(username);

       userAdminRepository.delete(userAdmin);

    }

    public UserAdmin getByUsername(String username){
            if(userAdminRepository.findByUsername(username) == null){
                throw new BadRequestException("no user");
            }
            return userAdminRepository.findByUsername(username);

    }
//    public Long totalSuccessfulOrdersOfUserAdmin(String username){
//        return orderStatusRepository.successfulOrdersOfUserAdmin(username);
//    }
    public Long countOfSuccessfulOrders(String username, LocalDate localDate){
        return orderStatusRepository.countOfAcceptedOrders(username, localDate);
    }
    public Long totalSumOfSuccessfulOrders(String username, LocalDate localDate){
        return orderStatusRepository.sumOfAcceptedOrders(username, localDate);
    }
    public List<OrderTimeService> getBookedTimes(LocalDate localDate, String username){

        List<LocalTime> starts =
                userAdminTimeTableRepository.getStartsByDate(localDate, username);
        List<LocalTime> ends =
                userAdminTimeTableRepository.getEndsByDate(localDate, username);
        List<String> serviceNames =
                userAdminTimeTableRepository.getServiceNames(localDate, username);
        List<OrderTimeService> booked = new ArrayList<>();
        if(!starts.isEmpty()) {
            for (int i = 0; i < starts.size(); i++) {
                booked.add(
                        new OrderTimeService(starts.get(i),
                                ends.get(i), serviceNames.get(i))
                );
            }
            return booked;
        }
        return null;
    }
//    @Cacheable(value = "companies",key = "#username")
    public String getCompanyName(String username){
        return companyRepository.getCompanyName(username);
    }
    private Company getCompanyByItsNameAndDirectorUsername(String companyName,
                                                           String username){
        return companyRepository.getByCompanyNameAndCompanyDirectorUsername(
                companyName, username);
    }
    public List<String> getOccupationNames(String companyName, String username){
        Company company =
                getCompanyByItsNameAndDirectorUsername(companyName, username);
        UserAdmin userAdmin =
                userAdminRepository.findByUsername(company.getDirectorUsername());
        if(company == null){
            return new ArrayList<>(Collections.singleton("no occupations"));
        }
        return serviceRepository.occupationNames(company.getCompanyId(), userAdmin.getUserAdminId());
    }

    public List<OrderView> getAllOrders(LocalDate date, String username, String status){
        List<Order> orders;
        if(status.equals("all")){
            orders = orderRepository.getAllOrdersByDate(date, username);
        }
        else {
            orders =
                    orderRepository.getAllOrders(date, getOrderStatus(status), username);
        }
        List<OrderView> orderViews = new ArrayList<>();
        for(Order or: orders){

            orderViews.add(
                    new OrderView(or.getStart(), or.getFinish(),
                            or.getClient().getUsername(), status, or.getServices().getOccupationName(),
                            or.getServices().getCompany().getCompanyName(), or.getOrderId(), or.getCreatedTime())
            );
        }
        return orderViews;
    }

    boolean ifUsernameExists(String newUsername){
        return userAdminRepository.existsByUsername(newUsername);

    }
    public static FullName getFullName(String fullName){
        FullName fullName1 = new FullName();
        for(int i = 0; i < fullName.length(); i++){
            char c = fullName.charAt(i);
            if(c == ' '){
                fullName1.setFirtsName(fullName.substring(0,i+1));
                char b = fullName.charAt(i+1);
                if(b != ' '){
                    fullName1.setLastName(fullName.substring(i+1, fullName.length()));
                }
            }
        }
        return fullName1;
    }

    public void updateUserDetails(String oldUsername, ChangeableUserDetails details){
        if(!ifUsernameExists(details.getUsername())) {
            UserAdmin userAdmin = getByUsername(oldUsername);
            userAdminRepository.updateByUserId
                        (details.getUsername(), userAdmin.getUserAdminId());

            FullName fullName = getFullName(details.getFullName());

            userAdmin.setFirstName(fullName.getFirtsName());
            userAdmin.setLastName(fullName.getLastName());
            userAdmin.setPhoneNumber(details.getPhoneNumbers());

            userAdminRepository.save(userAdmin);
            }

        throw new BadRequestException("This username is used!");
    }

    private static int getOrderStatus(String status){
        return switch (status){
            case "REQUESTED" -> 0;
            case "ACCEPTED" -> 1;
            case "DECLINED" -> -1;
            case "POSTPONED" -> -2;
            case "FINISHED" -> 2;
            case "BUSY" -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }
    private static String getStatusString(int num){
        return switch(num){
            case 2 -> "VERY GOOD";
            case 1 -> "GOOD";
            case 0 -> "NORMAL";
            case -1 -> "BAD";
            case -2 -> "VERY BAD";
            default -> throw new IllegalStateException("Unexpected status: " + num);
        };
    }

    public String getStatus(String username){
        return getStatusString(
                userAdminRepository.findByUsername(username).getUserAdminStatus().getAdminStatus());
    }
    public Order getOrderByOrderId(Long orderId){
        return orderRepository.getOrderByOrderId(orderId);
    }

    public void postpone(Order order, String date,
                         String newStartTime, String newEndTime, String username){
        UserAdmin userAdmin =
                userAdminRepository.findByUsername(username);

        order.setStart(LocalTime.parse(newStartTime));
        order.setFinish(LocalTime.parse(newEndTime));
        order.setDate(LocalDate.parse(date));
        OrderStatus orderStatus =
                orderStatusRepository.getByOrder_OrderId(order.getOrderId());

//        orderStatus.setEmployeeResponseStatus(OrderStatuses.POSTPONED);

        UserAdminTimetable userAdminTimetable =
                userAdminTimeTableRepository.getByOrder_OrderId(order.getOrderId());

        userAdminTimeTableRepository.delete(userAdminTimetable);
        if(userAdmin.getRole().equals("EMPLOYEE") || userAdmin.getRole().equals("EMPLOYER")) {
            orderStatus.setEmployeeResponseStatus(OrderStatuses.REQUESTED);//employee send a request to client
            orderStatus.setClientResponseStatus(OrderStatuses.WAITING);//client is waiting
        }
        else if (userAdmin.getRole().equals("CLIENT")){
            orderStatus.setEmployeeResponseStatus(OrderStatuses.WAITING);//client is sending a request to employee for given time about is it free
            orderStatus.setClientResponseStatus(OrderStatuses.REQUESTED);//employee should see and respond
        }
        orderRepository.save(order);

        orderStatusRepository.save(orderStatus);
    }

    public void changeStatusToDeclined(Long orderId) {
        orderStatusRepository.setOrderStatusToDeclined(OrderStatuses.DECLINED, orderId);
    }
    public void changeStatusToAccepted(Order order){
        var timeOfTimetable =
                UserAdminTimetable.builder()
                        .start(order.getStart())
                        .finish(order.getFinish())
                        .userAdmin(order.getUserAdmins())
                        .client(order.getClient())
                        .order(order)
                        .date(order.getDate())
                        .build();

        userAdminTimeTableRepository.save(timeOfTimetable);
        OrderStatus orderStatus =
                orderService.getOrderStatusByOrderId(order.getOrderId());

        orderStatusRepository.updateToBusy(OrderStatuses.BUSY,
                order.getDate(), order.getStart(),order.getFinish(),
                order.getUserAdmins().getUsername());
        orderStatus.setClientResponseStatus(OrderStatuses.ACCEPTED);
        orderStatus.setEmployeeResponseStatus(OrderStatuses.ACCEPTED);

        orderStatusRepository.save(orderStatus);
    }
    public UserAdminSettingsOfTimetable getSettingsOfTimetable(String username){
        return userAdminSettingOfTimetableRepository.findByusername(username);
    }
    public List<EmployeeView> getEmployees(Long categoryId) {

        List<EmployeeView> employeeViews = new ArrayList<>();
        List<String> occupationNames;

        List<Company> companiesOfThisCategory =
                companyRepository.findByCategoryId(categoryId);

        logger.info("Companies of category {}", companiesOfThisCategory);

        for (Company c : companiesOfThisCategory) {
            for (Long masterId : c.getMastersId()) {

                occupationNames =
                        serviceRepository.getServicesNamesOfThisUserAdminAndCategory(masterId);
                logger.info("Occupation names = {}", occupationNames);
                if (occupationNames != null) {
                    UserAdmin master =
                            userAdminRepository.getReferenceById(masterId);
                    logger.info("Master = {}", master);
                    employeeViews.add(
                            new EmployeeView(master.getRole(), master.getUserAdminImageName(),
                                    master.getUsername(), c.getCompanyName(), occupationNames,
                                    c.getAddress(), master.getUserAdminStatus().getRate(), master.getDetails())
                    );
                }
            }
        }
        return employeeViews;
    }


//    public SimpleMailMessage constructResetTokenEmail(
//            String contextPath, Locale locale, String token, String email) {
//        String url = contextPath + "/user/changePassword?token=" + token;
//        String message = "click this, if you want to change it or ignore if you don't send request";
//        return constructEmail("Reset Password", message + " \r\n" + url, email);
//    }
//    public SimpleMailMessage constructEmail(String subject, String body,
//                                             String emailTo) {
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setSubject(subject);
//        email.setText(body);
//        email.setTo(emailTo);
//        email.setFrom("contact@shopme.com");
//        return email;
//    }
//    public static String getSiteURL(HttpServletRequest request) {
//        String siteURL = request.getRequestURL().toString();
//        return siteURL.replace(request.getServletPath(), "");
//    }
//    public String getEmailOfUser(UserAdmin userAdmin){
//        return userAdmin.getEmail();
//    }
//    public String validatePasswordResetToken(String token) {
//        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
//
//        return !isTokenFound(passToken) ? "invalidToken"
//                : isTokenExpired(passToken) ? "expired"
//                : null;
//    }

//    private boolean isTokenFound(PasswordResetToken passToken) {
//        return passToken != null;
//    }
//
//    private boolean isTokenExpired(PasswordResetToken passToken) {
//        final Calendar cal = Calendar.getInstance();
//        return passToken.getExpiryDate().before(cal.getTime());
//    }
//    public Optional<Object> getUserByPasswordResetToken(String token){
//        PasswordResetToken passwordResetToken =
//                passwordResetTokenRepository.findByToken(token);
//        if(passwordResetToken.getUserAdmin() == null){
//            return Optional.of(passwordResetToken.getClient());
//        }
//        return Optional.of(passwordResetToken.getUserAdmin());
//    }
    public void checkUsername(String username){
        if(userAdminRepository.findByUsername(username) != null){
            throw new BadRequestException("Username is taken!");
        }
    }

    public List<String> mastersUsernames(List<Long> userAdminsId){
        return userAdminRepository.findUsernameOfUserAdmins(userAdminsId);
    }

}
