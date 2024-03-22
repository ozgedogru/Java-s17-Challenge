package com.workintech.spring17.controller;

import com.workintech.spring17.entity.Course;
import com.workintech.spring17.entity.HighCourseGpa;
import com.workintech.spring17.entity.LowCourseGpa;
import com.workintech.spring17.entity.MediumCourseGpa;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workintech/courses")
public class CourseController {

    private final List<Course> courses = new ArrayList<>();
    private final LowCourseGpa lowCourseGpa;
    private final MediumCourseGpa mediumCourseGpa;
    private final HighCourseGpa highCourseGpa;

    public CourseController(LowCourseGpa lowCourseGpa, MediumCourseGpa mediumCourseGpa, HighCourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courses;
    }

    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course) {

        for (Course existingCourse : courses) {
            if (existingCourse.getName().equals(course.getName())) {
                throw new RuntimeException("A course with the same name already exists.");
            }
        }
        if (course.getCredit() <= 0 || course.getCredit() > 4) {
            throw new RuntimeException("Credit value must be between 1 and 4.");
        }
        int totalGpa;
        if (course.getCredit() <= 2) {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        } else if (course.getCredit() == 3) {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        } else {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        }
        courses.add(course);
        return course;
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        for (Course course : courses) {
            if (course.getId() == id) {
                course.setName(updatedCourse.getName());
                course.setCredit(updatedCourse.getCredit());
                course.setGrade(updatedCourse.getGrade());
                return course;
            }
        }
        throw new RuntimeException("Course not found with id: " + id);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courses.removeIf(course -> course.getId() == id);
    }
}
