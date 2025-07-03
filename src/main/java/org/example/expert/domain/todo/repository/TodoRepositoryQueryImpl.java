package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery{

    private final JPAQueryFactory queryFactory;

    @Override
    public Todo findByIdWithUser(Long id) {
        QTodo todo = QTodo.todo;
        return Optional.ofNullable(queryFactory
                .selectFrom(todo)
                .where(todo.id.eq(id))
                .join(todo.user).fetchJoin()
                .fetchOne()).orElse(new Todo());

    }

    @Override
    public Page<TodoSearchResponse> searchTodos(String search, String nickname,
                                                LocalDateTime startDate, LocalDateTime endDate,
                                                Pageable pageable) {

        QTodo todo = QTodo.todo;
        QComment comment = QComment.comment;
        QManager manager = QManager.manager;

        BooleanBuilder builder = new BooleanBuilder();
        if (search != null) builder.and(todo.title.contains(search));
        if (nickname != null) builder.and(todo.user.nickname.contains(nickname));
        if (startDate != null && endDate != null) {
            builder.and(todo.createdAt.between(startDate, endDate));
        } else if (startDate != null) {
            builder.and(todo.createdAt.goe(startDate));
        } else if (endDate != null) {
            builder.and(todo.createdAt.loe(endDate));
        }

        // 서브쿼리: 댓글 수
        JPQLQuery<Long> commentCount = JPAExpressions
                .select(comment.count())
                .from(comment)
                .where(comment.todo.id.eq(todo.id));

        // 서브쿼리: 매니저 수
        JPQLQuery<Long> managerCount = JPAExpressions
                .select(manager.count())
                .from(manager)
                .where(manager.todo.id.eq(todo.id));

        // 본문 데이터 조회
        List<TodoSearchResponse> content = queryFactory
                .select(new QTodoSearchResponse(
                        todo.id,
                        todo.title,
                        managerCount,
                        commentCount
                ))
                .from(todo)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(builder)
                .fetchOne();

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                () -> Optional.ofNullable(total).orElse(0L));
    }

}
