package com.ddobang.backend.domain.diary.event;

import com.ddobang.backend.domain.member.entity.Member;
import com.ddobang.backend.domain.theme.entity.Theme;
import com.ddobang.backend.global.event.DomainEvent;

import lombok.Builder;
import lombok.Getter;

/**
 * DiaryStatChangedEvent
 * <p></p>
 * @author 100minha
 */
@Getter
@Builder
public class DiaryChangedEvent implements DomainEvent {

	private long memberId;
	private long themeId;

	@Override
	public String getEventType() {
		return "DIARY_STAT_CHANGED";
	}
}
