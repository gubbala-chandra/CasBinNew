package com.example.user.specification;

import com.example.user.entity.Group;
import com.example.user.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.util.Utils.isEmpty;

public class GroupSpecification {

    public static Specification<Group> getGroupSpecification(Long groupId, String groupName, String description, String status) {
        return (Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if(!isEmpty(groupId) && groupId != 0) {
                return builder.equal(root.get("groupId"), groupId);
            }
            if(!isEmpty(groupName)) {
                predicates.add(builder.like(root.get("groupName"), "%" + groupName + "%"));
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

            return builder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
