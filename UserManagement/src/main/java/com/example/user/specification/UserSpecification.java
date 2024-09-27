package com.example.user.specification;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.util.Utils.isEmpty;

public class UserSpecification {
    public static Specification<User> getRoleSpecification(Long userId, String userName, String email, String status) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!isEmpty(userId) && userId != 0) {
                predicates.add(builder.equal(root.get("userId"), userId));
            }

            if(!isEmpty(userName)) {
                predicates.add(builder.like(root.get("userName"), "%" + userName + "%"));
            }

            if(!isEmpty(email)) {
                predicates.add(builder.like(root.get("email"), "%" + email + "%"));
            }
            if(!isEmpty(status)) {
                predicates.add(builder.equal(root.get("status"), Status.valueOf(status)));
            }
            if (predicates.isEmpty()) {
                return builder.conjunction();
            }

            // Combine predicates with OR conjunction
            return builder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
