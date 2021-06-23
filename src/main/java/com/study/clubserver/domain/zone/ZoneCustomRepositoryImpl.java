package com.study.clubserver.domain.zone;

import static com.study.clubserver.domain.zone.QZone.zone;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ZoneCustomRepositoryImpl implements ZoneCustomRepository{

  private final JPAQueryFactory factory;

  @Override
  public List<Zone> findZonesByIdList(List<Long> idList) {
    return factory
      .select(zone)
      .from(zone)
      .where(isIn(idList))
      .fetch();
  }

  private BooleanExpression isIn(List<Long> idList) {
    if (idList == null || idList.isEmpty()) {
      return null;
    }
    return zone.id.in(idList);
  }
}
