package aimo.backend.common.mapper;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.member.dto.parameter.DeleteProfileImageParameter;
import aimo.backend.domains.member.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.entity.ProfileImage;
import aimo.backend.infrastructure.s3.dto.request.CreateResourceUrlParameter;
import aimo.backend.infrastructure.s3.dto.request.SaveFileMetaDataRequest;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;

public class S3Mapper {

	public static SaveFileMetaDataParameter toSaveFileMetaDataParameter(SaveFileMetaDataRequest request) {
		Long memberId = MemberLoader.getMemberId();
		return new SaveFileMetaDataParameter(memberId, request.filename(), request.extension(), request.size());
	}

	public static CreateResourceUrlParameter toCreateResourceUrlParameter(PresignedUrlPrefix prefix, SaveFileMetaDataParameter parameter) {
		return new CreateResourceUrlParameter(prefix.getValue(), parameter.filename(), parameter.extension());
	}

	public static ProfileImage toProfileImage(SaveFileMetaDataParameter parameter, Member member, String url) {
		return ProfileImage.builder()
			.member(member)
			.filename(parameter.filename())
			.extension(parameter.extension())
			.size(parameter.size())
			.url(url)
			.build();
	}

	public static DeleteProfileImageParameter toDeleteProfileImageParameter() {
		Long memberId = MemberLoader.getMemberId();
		return new DeleteProfileImageParameter(memberId);
	}
}
