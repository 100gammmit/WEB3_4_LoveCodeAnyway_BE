package com.ddobang.backend.domain.upload.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ddobang.backend.domain.upload.service.S3UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ImageEventListener
 * <p></p>
 * @author 100minha
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UploadEventListener {

	private final S3UploadService s3UploadService;
	private static final String EVENT_EXECUTOR = "s3EventExecutor";

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async(EVENT_EXECUTOR)
	public void onProfileImageChanged(ProfileImageChangedEvent event) {
		log.info("프로필 이미지 삭제 : {}", event.oldUrl());
		s3UploadService.delete(event.oldUrl());
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async(EVENT_EXECUTOR)
	public void onDiaryImageChanged(DiaryImageChangedEvent event) {
		log.info("방탈출 일지 이미지 삭제 : {}", event.oldUrl());
		s3UploadService.delete(event.oldUrl());
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async(EVENT_EXECUTOR)
	public void onAttachmentChanged(PostAttachmentsUpdatedEvent event) {
		event.oldUrls().forEach(s3UploadService::delete);
	}
}
