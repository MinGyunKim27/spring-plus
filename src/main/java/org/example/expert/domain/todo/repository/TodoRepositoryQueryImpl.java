package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery{

    private final JPAQueryFactory queryFactory;

    @Override
    public Todo findByIdWithUser(Long id) {
        QTodo todo = QTodo.todo;
        return queryFactory
                .selectFrom(todo)
                .where(todo.id.eq(id))
                .join(todo.user).fetchJoin()
                .fetchOne();

    }
}
