package com.udea.proyecto.repository;

import com.udea.proyecto.domain.Vuelo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vuelo entity.
 */
@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    default Optional<Vuelo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vuelo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vuelo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select vuelo from Vuelo vuelo left join fetch vuelo.avion", countQuery = "select count(vuelo) from Vuelo vuelo")
    Page<Vuelo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select vuelo from Vuelo vuelo left join fetch vuelo.avion")
    List<Vuelo> findAllWithToOneRelationships();

    @Query("select vuelo from Vuelo vuelo left join fetch vuelo.avion where vuelo.id =:id")
    Optional<Vuelo> findOneWithToOneRelationships(@Param("id") Long id);
}
