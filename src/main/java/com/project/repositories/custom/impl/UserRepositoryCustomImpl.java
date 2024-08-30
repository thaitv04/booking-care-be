package com.project.repositories.custom.impl;

import com.project.models.User;
import com.project.repositories.custom.UserRepositoryCustom;
import com.project.requests.UserSearchRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.List;

@Repository
@Primary
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Hàm này để tạo ra câu where với điều kiện = hoặc like
     *
     * @param request - các giá trị nhận từ client
     * @param where     - câu where gốc
     */
    private void createWhereQueryNormal(UserSearchRequest request, StringBuilder where) {
        try {
            Field[] fields = UserSearchRequest.class.getDeclaredFields();
            for (Field item : fields) {
                item.setAccessible(true);
                String fieldName = item.getName();
                if (!fieldName.startsWith("dateOfBirth") && !fieldName.startsWith("leave") && !fieldName.startsWith("experience")
                        && !fieldName.startsWith("certification") && !fieldName.startsWith("major")){
                    Object value = item.get(request);
                    if (value != null && value != "") {
                        switch (item.getType().getName()) {
                            case "java.lang.Integer":
                            case "java.lang.Long":
                                where.append(" AND u.").append(fieldName).append(" = ").append(value).append(" ");
                                break;
                            case "java.lang.String":
                                where.append(" AND u.").append(fieldName).append(" like '%").append(value).append("%' ");
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm này để tạo ra câu where với các điều kiện đặc biệt như <= hoặc các trường phải nối bảng,
     *
     * @param request - các giá trị nhận từ client
     * @param where     - câu where gốc
     */
    private void createWhereQuerySpecial(UserSearchRequest request, StringBuilder where) {
        Integer certificationFrom = request.getCertificationFrom();
        Integer certificationTo = request.getCertificationTo();
        if (certificationTo != null) {
            where.append(" AND u.certification <= ").append(certificationTo);
        }
        if (certificationFrom != null) {
            where.append(" AND u.certification >= ").append(certificationFrom);
        }

        Integer experienceFrom = request.getExperienceFrom();
        Integer experienceTo = request.getExperienceTo();
        if (experienceTo != null) {
            where.append(" AND u.experience <= ").append(experienceTo);
        }
        if (experienceFrom != null) {
            where.append(" AND u.experience >= ").append(experienceFrom);
        }

        Integer leaveFrom = request.getLeaveFrom();
        Integer leaveTo = request.getLeaveTo();
        if (leaveTo != null) {
            where.append(" AND u.deletedyear <= ").append(leaveTo);
        }
        if (leaveFrom != null) {
            where.append(" AND u.deletedyear >= ").append(leaveFrom);
        }

        String dateOfBirthFrom = request.getDateOfBirthFrom();
        String dateOfBirthTo = request.getDateOfBirthTo();
        if (dateOfBirthTo != null && !dateOfBirthTo.isEmpty()) {
            where.append(" AND u.dateofbirth <= '").append(dateOfBirthTo).append("'");
        }
        if (dateOfBirthFrom != null && !dateOfBirthFrom.isEmpty()) {
            where.append(" AND u.dateofbirth >= '").append(dateOfBirthFrom).append("'");
        }

        Long majorId = request.getMajor();
        if (majorId != null) {
            where.append(" AND u.major_id = ").append(majorId);
        }
    }

    @Override
    public List<User> getAllUsers(UserSearchRequest request) {
        StringBuilder sql = new StringBuilder("SELECT u.* FROM user u ");
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 AND role_id = 2 ");
        createWhereQueryNormal(request, where);
        createWhereQuerySpecial(request, where);
        sql.append(where);
        sql.append(" GROUP BY u.id ");
        Query query = entityManager.createNativeQuery(sql.toString(), User.class);
        return query.getResultList();
    }
}
