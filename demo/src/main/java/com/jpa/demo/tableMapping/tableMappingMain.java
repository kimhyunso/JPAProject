package com.jpa.demo.tableMapping;

import com.jpa.demo.tableMapping.domain.Member;
import com.jpa.demo.tableMapping.domain.NotJPAMember;
import com.jpa.demo.tableMapping.domain.NotJPATeam;
import com.jpa.demo.tableMapping.domain.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class tableMappingMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction ex = em.getTransaction();
        ex.begin();

        // notJpaLogic();
        // testSave(em);
        // findTeam(em);
        queryLogicJoin(em);

        ex.commit();
    }

    private static void notJpaLogic(){
        NotJPAMember member1  = NotJPAMember.builder()
                .id("member1")
                .username("화원1")
                .build();

        NotJPAMember member2  = NotJPAMember.builder()
                .id("member2")
                .username("화원2")
                .build();

        NotJPATeam team = NotJPATeam.builder()
                .id("team1")
                .name("팀1")
                .build();

        member1.setTeam(team);
        member2.setTeam(team);

        // 객체 그래프 탐색
        NotJPATeam findTeam = member1.getTeam();
        System.out.println("findTeam.id : " + findTeam.getId() + ", Name : " + findTeam.getName());
    }

    private static void testSave(EntityManager em){
        Team team = Team.builder()
                .id("team1")
                .name("팀1")
                .build();


        em.persist(team);


        Member member1 = Member.builder()
                .id("member1")
                .username("회원1")
                .team(team) // 연관관계 설정 member1 -> team1
                .build();
        em.persist(member1);

        Member member2 = Member.builder()
                .id("member2")
                .username("회원2")
                .team(team) // 연관관계 설정 member2 -> team1
                .build();
        em.persist(member2);

    }

    private static void findTeam(EntityManager em){
        Member member = em.find(Member.class, "member1");
        // 객체 그래프 탐색
        Team team = member.getTeam();
        System.out.println("팀 이름 : " + team.getName());
    }

    private static void queryLogicJoin(EntityManager em){
        String jpql = "select m from Member m join m.team t where " +
                "t.name=:teamName";

        // 파라미터를 바인딩 받는 문법 =:
        // MEMBER TABLE과 TEAM TABLE이 조인된 결과=(MEMBER 컬럼들만)를 가져옴
        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member member : resultList){
            System.out.println("[query] member team : " + member.getTeam().getName());
            System.out.println("[query] member.username : " + member.getUsername());
        }
    }

}
