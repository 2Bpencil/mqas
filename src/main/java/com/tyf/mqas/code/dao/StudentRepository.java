package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public interface StudentRepository extends ExpandJpaRepository<Student,Integer>{

    @Query(value = "select count(*) from student where authority = ?2 and id != ?1", nativeQuery = true)
    Integer getStudentNumByIdAndAuthority(Integer id, String authority);

    @Query(value = "select count(*) from student where authority = ?1", nativeQuery = true)
    Integer getStudentNumByAuthority(String authority);





    Student getStudentById(Integer id);

    /**
     * 根据班级id获取班级学生数量
     * @param id
     * @return
     */
    @Query(value = "select count(*) from student  where classes_id = ?1", nativeQuery = true)
    Integer getStudentNumByClassesId(Integer id);

    @Query(value = "select count(*) from student stu left join classes cla on cla.id = stu.classes_id  where stu.classes_id IN (SELECT c.id FROM classes c  WHERE pid = ?1)  and cla.name LIKE  CONCAT('%',?2,'%') ", nativeQuery = true)
    Integer getStudentNumByClassesIdAndSubject(Integer id,String subject);
    @Query(value = "select count(*) from student stu left join classes cla on cla.id = stu.classes_id  where stu.classes_id IN (SELECT c.id FROM classes c  WHERE pid = ?1) ", nativeQuery = true)
    Integer getStudentNumByGradeId(Integer id);

    /**
     * 获取所有学生总数
      * @return
     */
    @Query(value = "select count(*) from student ", nativeQuery = true)
    Integer getAllStudentNum();


}