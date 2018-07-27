package com.cts.iiht.fsd.skilltracker.associate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.iiht.fsd.skilltracker.framework.entity.Associate;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long>{
	
	//@Query(value = "SELECT a.id, a.associate_id, a.associate_name, a.email, a.mobile, a.status_blue, a.status_green, a.status_red, GROUP_CONCAT(s.skill_name) FROM ASSOCIATE_DETAILS a LEFT OUTER JOIN ASSOCIATE_SKILL_DETAILS ask ON a.id=ask.associate_id LEFT OUTER JOIN SKILL_DETAILS s ON s.skill_id = ask.skill_id AND ask.skill_level > 0 GROUP BY ask.associate_id", nativeQuery = true)
	@Query(value = "SELECT a.id, a.associate_id, a.associate_name, a.email, a.mobile, a.status_blue, a.status_green, a.status_red, '' from ASSOCIATE_DETAILS a where not exists (select * from ASSOCIATE_SKILL_DETAILS ask where ask.associate_id = a.id) UNION all SELECT a.id, a.associate_id, a.associate_name, a.email, a.mobile, a.status_blue, a.status_green, a.status_red, GROUP_CONCAT(s.skill_name) FROM ASSOCIATE_DETAILS a  INNER JOIN ASSOCIATE_SKILL_DETAILS ask ON a.id=ask.associate_id INNER JOIN SKILL_DETAILS s ON s.skill_id = ask.skill_id AND ask.skill_level > 0 GROUP BY ask.associate_id", nativeQuery = true)
    public List<Object[]> getAssociatesDetailsList();
	
	@Query(value = "SELECT gender, count(*) from ASSOCIATE_DETAILS group by gender", nativeQuery = true)
	public List<Object[]> getCandidatesCountByGender();
	
	@Query(value = "SELECT count(*) from ASSOCIATE_DETAILS where level1 = true", nativeQuery = true)
	public Object[] getFreshersCount();
	
	@Query(value = "SELECT a.gender, count(DISTINCT a.id) from ASSOCIATE_DETAILS a LEFT OUTER JOIN ASSOCIATE_SKILL_DETAILS ask ON a.id = ask.associate_id WHERE ask.skill_level > 0 GROUP BY a.gender", nativeQuery = true)
	public List<Object[]> getRatedCandidatesByGender();
	
	@Query(value = "SELECT count(*) from ASSOCIATE_DETAILS where level1 = true", nativeQuery = true)
	public Object[] getLevel1CandidatesCount();

	@Query(value = "SELECT count(*) from ASSOCIATE_DETAILS where level2 = true", nativeQuery = true)
	public Object[] getLevel2CandidatesCount();
	
	@Query(value = "SELECT count(*) from ASSOCIATE_DETAILS where level3 = true", nativeQuery = true)
	public Object[] getLevel3CandidatesCount();
	
	@Query(value = "SELECT s.skill_id, s.skill_name, ask.skill_level from SKILL_DETAILS s INNER JOIN ASSOCIATE_SKILL_DETAILS ask ON s.skill_id = ask.skill_id where ask.associate_id = :associateId", nativeQuery = true)
	public List<Object[]> getSkillsByAssociateId(@Param("associateId") Long associateId);
}
