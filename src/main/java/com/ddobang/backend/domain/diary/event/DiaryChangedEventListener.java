package com.ddobang.backend.domain.diary.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ddobang.backend.domain.member.support.MemberStatCalculator;
import com.ddobang.backend.domain.theme.support.ThemeStatCalculator;
import com.ddobang.backend.global.util.OptimisticLockRetryExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DiaryChangedEventListener
 * <p></p>
 * @author 100minha
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryChangedEventListener {

	private final OptimisticLockRetryExecutor retryExecutor;
	private final MemberStatCalculator memberStatCalculator;
	private final ThemeStatCalculator themeStatCalculator;

	private static final String EVENT_EXECUTOR = "statEventExecutor";

	@Async(EVENT_EXECUTOR)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(DiaryChangedEvent event) {
		long themeId = event.getThemeId();
		long memberId = event.getMemberId();
		log.debug("DiaryChangedEvent published(themeId = {}, memberId = {})", themeId, memberId);

		updateThemeStat(themeId);
		updateMemberStat(memberId);
	}

	private void updateThemeStat(long themeId) {

		try {
			retryExecutor.run("themeId:" + themeId,
				() -> themeStatCalculator.updateThemeStat(themeId));
		} catch (Exception e) {
			log.error("Theme stat update failed. themeId={}", themeId, e);
		}

	}

	private void updateMemberStat(long memberId) {

		try {
			retryExecutor.run("memberId:" + memberId,
				() -> memberStatCalculator.updateMemberStat(memberId));
		} catch (Exception e) {
			log.error("Member stat update failed. memberId={}", memberId, e);
		}
	}
}
