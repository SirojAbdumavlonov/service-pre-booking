package com.example.preordering.service;

import com.example.preordering.constants.GeneralStatuses;
import com.example.preordering.constants.OrderStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.*;
import com.example.preordering.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class UserAdminService {

    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;
    private final ClientsStatusRepository clientsStatusRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;
    private final CompanyRepository companyRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    public void deleteUser(String username){
        if(getByUsername(username) instanceof UserAdmin userAdmin){
            userAdminRepository.delete(userAdmin);
        }
        else if(getByUsername(username) instanceof Client client){
            clientRepository.delete(client);
        }
    }
    public Object getByUsername(String username){
        if(clientRepository.findByUsername(username) == null){
            if(userAdminRepository.findByUsername(username) == null){
                throw new UsernameNotFoundException("no user");
            }
            return userAdminRepository.findByUsername(username);
        }
        return clientRepository.findByUsername(username);
    }
    public Long totalSuccessfulOrdersOfUserAdmin(String username){
        return orderStatusRepository.successfulOrdersOfUserAdmin(username);
    }
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
        if(company == null){
            return new ArrayList<>(Collections.singleton("no occupations"));
        }
        return serviceRepository.occupationNames(company.getCompanyId(),username);
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
        return userAdminRepository.existsByUsername(newUsername) ||
                clientRepository.existsByUsername(newUsername);
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
        if(ifUsernameExists(details.getUsername())) {
            if (getByUsername(oldUsername) instanceof UserAdmin userAdmin) {
                userAdminRepository.updateByUsername(details.getUsername(), oldUsername);
                FullName fullName = getFullName(details.getFullName());
                userAdmin.setFirstName(fullName.getFirtsName());
                userAdmin.setLastName(fullName.getLastName());
                userAdmin.setPhoneNumber(details.getPhoneNumbers());

            }
            else if (getByUsername(oldUsername) instanceof Client client)
            {
                clientRepository.updateByUsername(details.getUsername(), oldUsername);
                FullName fullName = getFullName(details.getFullName());
                client.setFirstName(fullName.getFirtsName());
                client.setLastName(fullName.getLastName());
                client.setPhoneNumber(details.getPhoneNumbers());
            }
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
    public Order getOrderByClientUsernameAndOrderId(String username, Long orderId){
        return orderRepository.getOrderByOrderIdAndClientUsernameAndStatus(orderId, username, GeneralStatuses.ACTIVE);
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
        orderRepository.delete(order);
        orderStatusRepository.delete(orderStatus);
    }
    public UserAdminSettingsOfTimetable getSettingsOfTimetable(String username){
        return userAdminRepository.findByUsername(username).getUserAdminSettingsOfTimetable();
    }
    public List<EmployeeView> getEmployees(Long categoryId) {

        List<EmployeeView> employeeViews = new ArrayList<>();
        List<String> occupationNames;

        List<Company> companiesOfThisCategory =
                companyRepository.findByCategoryId(categoryId);

        for (Company c : companiesOfThisCategory) {
            for (String masterUsername : c.getMastersUsernames()) {


                occupationNames = serviceRepository.getServicesNamesOfThisUserAdminAndCategory(masterUsername);

                if (occupationNames != null) {
                    UserAdmin master =
                            userAdminRepository.findByUsername(masterUsername);

                    employeeViews.add(
                            new EmployeeView(master.getRole(), master.getUserAdminImageName(),
                                    masterUsername, c.getCompanyName(), occupationNames,
                                    c.getAddress(), master.getUserAdminStatus().getRate())
                    );
                }
            }
        }
        return employeeViews;
    }


    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, String email) {
        String url = contextPath + "/user/changePassword?token=" + token;
        String message = "click this, if you want to change it or ignore if you don't send request";
        return constructEmail("Reset Password", message + " \r\n" + url, email);
    }
    public SimpleMailMessage constructEmail(String subject, String body,
                                             String emailTo) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(emailTo);
        email.setFrom("contact@shopme.com");
        return email;
    }
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    public String getEmailOfUser(Object user){
        if(user instanceof UserAdmin userAdmin){
            return userAdmin.getEmail();
        }
        else if (user instanceof Client client) {
            return client.getEmail();
        }
        return null;
    }
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
    public Optional<Object> getUserByPasswordResetToken(String token){
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken.getUserAdmin() == null){
            return Optional.of(passwordResetToken.getClient());
        }
        return Optional.of(passwordResetToken.getUserAdmin());

    }

}
