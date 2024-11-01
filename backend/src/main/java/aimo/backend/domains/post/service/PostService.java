package aimo.backend.domains.post.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.mapper.PostMapper;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.dto.SavePostRequest;
import aimo.backend.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;

	public void save(SavePostRequest savePostRequest, Member member) {
		postRepository.save(PostMapper.toEntity(savePostRequest, member));
	}
}
