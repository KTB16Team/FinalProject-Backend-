package aimo.backend.domains.view.repository;

import org.springframework.data.repository.CrudRepository;

import aimo.backend.domains.view.entity.PostView;

public interface PostViewRepository extends CrudRepository<PostView, String> {
}
