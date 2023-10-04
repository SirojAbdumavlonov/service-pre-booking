package com.example.preordering.entity;

import com.example.preordering.constants.DefaultSettingsOfUserAdminTimetable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserAdminSettingsOfTimetable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "settings_id")
    private Long id;

    @OneToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "userAdmin_id"
    )
    private UserAdmin userAdmin;



    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime start;

    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime finish = DefaultSettingsOfUserAdminTimetable.END;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    private Long breakInMinutesBetweenOrders = 0L;

    @Temporal(TemporalType.TIME)
    private LocalTime breakStart;

    @Temporal(TemporalType.TIME)
    private LocalTime breakFinish;

    private List<DayOfWeek> weekendDays;
}
