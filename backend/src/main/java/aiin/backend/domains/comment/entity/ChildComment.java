package aiin.backend.domains.comment.entity;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "child_comments")
@NoArgsConstructor(access = PROTECTED)
public class ChildComment extends Comment {

	@Column(nullable = false)
	private Long parentId;
}
