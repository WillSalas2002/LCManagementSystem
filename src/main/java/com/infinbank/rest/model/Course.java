package com.infinbank.rest.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course", schema = "public")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "course_name")
    private String courseName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private List<Teacher> teachers;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
}
