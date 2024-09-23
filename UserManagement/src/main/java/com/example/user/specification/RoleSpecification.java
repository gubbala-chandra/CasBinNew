package com.example.user.specification;

import com.example.user.entity.Role;
import com.example.user.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.util.Utils.isEmpty;

public class RoleSpecification {

    public static Specification<Role> getRoleSpecification(Long roleId, String roleName, String description, String status) {
        return (Root<Role> root,CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!isEmpty(roleId) && roleId != 0) {
                predicates.add(builder.equal(root.get("roleId"), roleId));
            }

            if(!isEmpty(roleName)) {
                predicates.add(builder.like(root.get("roleName"), "%" + roleId + "%"));
            }

            if(!isEmpty(description)) {
                predicates.add(builder.like(root.get("description"), "%" + description + "%"));
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
