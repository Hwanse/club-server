package com.study.clubserver.domain.zone;

import java.util.List;

public interface ZoneCustomRepository {

  List<Zone> findZonesByIdList(List<Long> idList);

}
