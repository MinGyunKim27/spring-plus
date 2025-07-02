package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // pageble 만 존재 할 경우
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // 날씨만 존재 할 경우
    @EntityGraph(attributePaths = "user")
    @Query("SELECT t FROM Todo t WHERE t.weather LIKE %:weather% ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherContainingOrderByModifiedAtDesc(@Param("weather") String weather,
                                                            Pageable pageable);

    //날씨, start , endDate 모두 존재 할 경우
    @Query("SELECT t FROM Todo t WHERE t.weather LIKE %:weather% AND t.modifiedAt BETWEEN :start AND :end ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndDateRange(@Param("weather") String weather,
                                         @Param("start") LocalDate start,
                                         @Param("end") LocalDate end,
                                         Pageable pageable);

    // start , endDate만 존재 할 경우
    @Query("SELECT t FROM Todo t WHERE t.modifiedAt BETWEEN :start AND :end ORDER BY t.modifiedAt DESC")
    Page<Todo> findByDateRange(@Param("start") LocalDate start,
                               @Param("end") LocalDate end,
                               Pageable pageable);


    // endDate 만 존재 할 경우
    @Query("SELECT t FROM Todo t WHERE t.modifiedAt <= :end ORDER BY t.modifiedAt DESC")
    Page<Todo> findByEndDate(@Param("end") LocalDate end, Pageable pageable);



    // startDate만 있을 경우
    @Query("SELECT t FROM Todo t WHERE t.modifiedAt >= :start ORDER BY t.modifiedAt DESC")
    Page<Todo> findByStartDate(@Param("start") LocalDate start, Pageable pageable);


    //날씨와 startDate만 있을 경우
    @Query("""
    SELECT t FROM Todo t
    WHERE t.weather LIKE %:weather%
    AND t.modifiedAt >= :startDate
    ORDER BY t.modifiedAt DESC
""")
    Page<Todo> findByWeatherFromStartDate(@Param("weather") String weather,
                                          @Param("startDate") LocalDate startDate,
                                          Pageable pageable);


    @Query("""
    SELECT t FROM Todo t
    WHERE t.weather LIKE %:weather%
    AND t.modifiedAt <= :endDate
    ORDER BY t.modifiedAt DESC
""")
    Page<Todo> findByWeatherUntilEndDate(@Param("weather") String weather,
                                         @Param("endDate") LocalDate endDate,
                                         Pageable pageable);



    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

}
