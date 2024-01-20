package com.jpa.demo.queryLanguage;

import com.jpa.demo.queryLanguage.domain1.*;
import com.jpa.demo.queryLanguage.domain3.Album;
import com.jpa.demo.queryLanguage.domain3.Book;
import com.jpa.demo.queryLanguage.domain3.Movie;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");


    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            // jpqlSelect(em);
            // criteriaSelect(em);
            // nativeSQLSelect(em);
            // testProjection(em);
            // newTest(em);
            // jpqlJoinQuery(em);
            // jpqlOuterQuery(em);
            // jpqlCollectionJoin(em);
            // jpqlProjectionQuery(em);
            // init(em);
            // sataJoinTest(em);
            // joinOnTest(em);
            // fetchJoin(em);
            // init2(em);
            // insertData(em);
            /// dialectTest(em);
            // namedQueryTest(em);
            testQuery(em);
            tx.commit();
        }catch (Exception e){
            System.out.println("처리오류 : " + e.getMessage());
            tx.rollback();
        }
    }


    public static void jpqlSelect(EntityManager em) {
        // init(em);

        // 1. 대소문자 구분함
        // 2. 별칭(식별변수)은 무조건 줘야한다.

        /**
         *  반환할 타입이 명확 => TypeQuery
         *  반환 타입이 불명확 => Query
         */

//        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.username = 'kim'", Member.class);
//
//        List<Member> resultList = query.getResultList();
//        for (Member member : resultList){
//            System.out.println("팀이름 : " + member.getTeam().getTeamName());
//            System.out.println("멤버이름 : " + member.getUsername());
//        }
//
//        Query query1 = em.createQuery("SELECT m.username, m.age FROM Member m");
//        List<Member> resultList1 = query1.getResultList();
//        for (Member member : resultList){
//            System.out.println("팀이름 : " + member.getTeam().getTeamName());
//            System.out.println("멤버이름 : " + member.getUsername());
//        }
//
//        String jpql = "select m from Member m where m.age >= 30";
//        List<Member> members = em.createQuery(jpql, Member.class).getResultList();
//
//        for (Member member : members){
//            System.out.println("팀이름 : " + member.getTeam().getTeamName());
//            System.out.println("멤버이름 : " + member.getUsername());
//        }
        // 파라미터 바인딩
//        String usernameParam = "kim";
//
//        TypedQuery<Member> query =
//                em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class);
//        query.setParameter("username", usernameParam);
//
//        List<Member> members = query.getResultList();
        // 메소드 체인 방식
//        String usernameParam = "kim";
//        List<Member> members =
//                em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
//                        .setParameter("username", usernameParam)
//                        .getResultList();

        // 위치 기준 파라미터
        String usernameParam = "park";
        List<Member> members = em.createQuery("SELECT m FROM Member m WHERE m.username = ?1", Member.class)
                .setParameter(1, usernameParam)
                .getResultList();

        for (Member member : members) {
            System.out.println("팀이름 : " + member.getTeam().getTeamName());
            System.out.println("멤버이름 : " + member.getUsername());
        }
    }


    public static void criteriaSelect(EntityManager em){
        // Criteria 사용준비
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = builder.createQuery(Member.class);

        // 루트 클래스 (조회를 시작할 클래스)
        Root<Member> m = query.from(Member.class);
//
//        CriteriaQuery<Member> cq = query.select(m)
//                .where(builder.equal(m.get("age"), 30));

        CriteriaQuery<Member> cq = query.select(m)
                .where(builder.equal(m.get("age"), 30));

        List<Member> members = em.createQuery(cq).getResultList();

        for (Member member : members){
            System.out.println("팀이름 : " + member.getTeam().getTeamName());
            System.out.println("멤버이름 : " + member.getUsername());
        }
    }


    public static void queryDSLSelect(EntityManager em){
        // JPAQuery query = new JPAQuery(em);
        // QMember member = QMember.member;

    }


    public static void nativeSQLSelect(EntityManager em){
        String sql = "SELECT * FROM MEMBER WHERE NAME = 'kim'";

        List<Member> members = em.createNativeQuery(sql, Member.class).getResultList();

        for (Member member : members){
            System.out.println("팀이름" + member.getTeam().getTeamName());
            System.out.println("유저이름" + member.getUsername());
        }
    }

    public static void getSession(EntityManager em){
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                // work.. 우회하는 방법
            }
        });

    }


    /**
     *  프로젝션 : SELECT 절에 조회할 대상을 지정하는 것
     *  1. 엔티티
     *  2. 임베디드
     *  3. 스칼라 타입 (문자, 숫자..)
     *  # 엔티티 프로젝션
     *  SELECT m FROM Member m
     *  SELECT m.team FROM Member m
     *  --- 영속성 컨텍스에서 관리 ---
     *
     *  # 임베디드 프로젝션
     *  SELECT a FROM Address a (x)
     *  SELECT m.Address FROM Member m (o)
     *  --- 영속성 컨텍스트에서 관리 x ---
     *  
     *  # 스칼라 타입
     *  SELECT m.username, m.age FROM Member
     *
     */

    public static void testProjection(EntityManager em){
        List<Object[]> listResult = em.createQuery("SELECT m.username, m.age FROM Member m").getResultList();

        Iterator iterator = listResult.iterator();

        List<UserDTO> userDTOList = new ArrayList<>();

        while (iterator.hasNext()){
            Object[] row = (Object[]) iterator.next();
            UserDTO userDTO = new UserDTO((String)row[0], (Integer) row[1]);
            String username = (String)row[0];
            Integer age = (Integer) row[1];
            userDTOList.add(userDTO);
            System.out.println("이름 : " + username + " 나이 : " + age);
        }
    }
    /**
     * QueryType => 엔티티 타입으로 조회할 때
     * Query => 스칼라, 임베디드, 엔티티 타입도 조회할 때 사용
     *
     * ex) SELECT o.member, o.product, o.orderAmount FROM Order o
     *
     */


    public static void newTest(EntityManager em){

        // 1. 패키지 명을 포함한 클래스 명을 작성
        // 2. 생성자가 있어야함


        // FirstResult : 조회 시작 위치 11 ~ 30 (20건의 데이터 조회)
        // MaxResults : (20건 데이터를 조회)
        List<UserDTO> userDTOList = em.createQuery("SELECT new com.jpa.demo.queryLanguage.domain1.UserDTO(m.username, m.age) FROM Member m", UserDTO.class)
                .setFirstResult(10)
                .setMaxResults(20)
                .getResultList();

        for (UserDTO dto : userDTOList){
            System.out.println("이름 : " + dto.getUsername());
            System.out.println("나이 : " + dto.getAge());
        }

    }

    public static void jpqlJoinQuery(EntityManager em){
        String teamName = "좋아요";
        String query = "SELECT new com.jpa.demo.queryLanguage.domain1.UserDTO(m.username, m.age) "
                + "FROM Member m "
                + "INNER JOIN m.team t "
                + "WHERE t.teamName = :teamName "
                + "ORDER BY m.age DESC";

        List<UserDTO> members = em.createQuery(query, UserDTO.class)
                .setParameter("teamName", teamName)
                .getResultList();

        for (UserDTO member : members){
            System.out.println("이름 : " + member.getUsername());
            System.out.println("나이 : " + member.getAge());
        }
    }

    public static void jpqlOuterQuery(EntityManager em){
        String teamName = "좋아요";
        String query = "SELECT new com.jpa.demo.queryLanguage.domain1.UserDTO(m.username, m.age) "
                + "FROM Member m "
                + "LEFT OUTER JOIN m.team t "
                + "WHERE t.teamName = :teamName "
                + "ORDER BY m.age DESC";
        // Query
        List<UserDTO> members = em.createQuery(query, UserDTO.class)
                .setParameter("teamName", teamName)
                .getResultList();

        for (UserDTO member : members){
            System.out.println("이름 : " + member.getUsername());
            System.out.println("나이 : " + member.getAge());
        }
    }

    public static void jpqlCollectionJoin(EntityManager em){
        String sql = "SELECT t "
                + "FROM Team t "
                + "LEFT JOIN t.members m";

        // QueryType
        List<Team> teams = em.createQuery(sql, Team.class)
                .getResultList();

        teams.stream().forEach(team -> {
            for (Member member : team.getMembers()){
                System.out.println("팀이름 : " + team.getTeamName() + ", 멤버이름 : " + member.getUsername() + ", 멤버나이 : " + member.getAge());
            }
        });

//        List<Object[]> teams = em.createQuery(sql)
//                .getResultList();
//
//        Iterator iterator = teams.iterator();
//
//        while (iterator.hasNext()){
//            Object o = iterator.next();
//            Team team = (Team) o;
//            System.out.println("팀이름 : " + team.getTeamName());
//            for (Member member : team.getMembers()){
//                System.out.println("멤버이름 : " + member.getUsername() + ", 멤버나이 : " + member.getAge());
//            }
//        }
    }


    public static void jpqlProjectionQuery(EntityManager em){

        // Query로만 조회가능 TypeQuery로는 조회 x
        String sql = "SELECT o.members, o.product, o.orderAmount, o.address FROM Order o";

        List<Object[]> query = em.createQuery(sql).getResultList();

        for (Object[] row : query){
            Member member = (Member) row[0];
            Product product = (Product) row[1];
            int orderAmount = (Integer) row[2];
            Address address = (Address) row[3];

            System.out.println("주문자 : " + member.getUsername() + ", 나이 : " + member.getAge());
            System.out.println("주문상품 : " + product.getName() + ", 가격 : " + product.getPrice() + ", 구매갯수 : " + orderAmount);
            System.out.println("주소 : " + address.getCity() + address.getStreet() + address.getZipcode());

        }
    }

    public static void sataJoinTest(EntityManager em){
        String sql = "SELECT COUNT(m) FROM Member m, Team t "
                + "WHERE m.username = t.teamName";

         Integer count = em.createQuery(sql).getFirstResult();

        System.out.println(count);
    }


    // TODO: 확인하기
    public static void joinOnTest(EntityManager em){
//        String sql = "SELECT m, t FROM Member m "
//                + "LEFT JOIN m.team t "
//                + "ON t.name = '좋아요' ";
//
//        String sql = "SELECT t.members FROM Team t";
//
//        List<com.jpa.demo.queryLanguage.domain2.Member> resultList = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Member.class).getResultList();
//        resultList.stream().forEach(member -> {
//            System.out.println("멤버이름 : " + member.getName());
//            System.out.println("멤버나이 : " + member.getAge());
//        });

//        String sql = "SELECT m.team FROM Member m";
//
//        List<com.jpa.demo.queryLanguage.domain2.Team> teams = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Team.class).getResultList();
//
//        teams.stream().forEach(team -> {
//            System.out.println("팀이름 : " + team.getName());
//        });

        String sql = "SELECT t.members.size FROM Team t";

        Integer memberCount = em.createQuery(sql).getFirstResult();

        System.out.println(memberCount);


//        for (Object[] result : resultList){
            // com.jpa.demo.queryLanguage.domain2.Member member = (com.jpa.demo.queryLanguage.domain2.Member) result[0];
            // com.jpa.demo.queryLanguage.domain2.Team team = (com.jpa.demo.queryLanguage.domain2.Team) result[1];
            // System.out.println("팀이름 : " + team.getName());
            // System.out.println("이름 : " + member.getName() + ", 나이 " + member.getAge());
//        }
    }

    public static void dialectTest(EntityManager em){
//        String sql = "SELECT group_concat(i.name) FROM Item i";
//        List result = em.createQuery(sql).getResultList();
//        result.stream().forEach(i-> System.out.println(i));

        // SELECT m FROM Member m  => (m.id)

        com.jpa.demo.queryLanguage.domain2.Member member = new com.jpa.demo.queryLanguage.domain2.Member();
        member.setId(3L);

        String sql = "SELECT m FROM Member m WHERE m = :member";
        // sql = "SELECT m FROM Member m WHERE m.id = :memberId";

        List result = em.createQuery(sql)
                .setParameter("member", member)
                .getResultList();

        result.stream().forEach(item -> {
            com.jpa.demo.queryLanguage.domain2.Member m = (com.jpa.demo.queryLanguage.domain2.Member) item;
            System.out.println("이름 : " + m.getName() + ", 나이 : " + m.getAge());
        });


        com.jpa.demo.queryLanguage.domain2.Team team = em.find(com.jpa.demo.queryLanguage.domain2.Team.class, 1L);

        sql = "SELECT m FROM Member m WHERE m.team = :team";
        // sql = "SELECT m FROM Member m WHERE m.team.id = :teamId";

        result = em.createQuery(sql)
                .setParameter("team", team)
                .getResultList();

        result.stream().forEach(item -> {
            com.jpa.demo.queryLanguage.domain2.Member m = (com.jpa.demo.queryLanguage.domain2.Member) item;
            System.out.println("팀이름 : " + m.getTeam().getName() + ", 회원이름 : " + m.getName() + ", 회원나이 : " + m.getAge());
        });

    }

    public static void namedQueryTest(EntityManager em){
        List<com.jpa.demo.queryLanguage.domain2.Member> members = em.createNamedQuery("Member.findByUsername", com.jpa.demo.queryLanguage.domain2.Member.class)
                .setParameter("name", "MemberE")
                .getResultList();

        members.stream().forEach(member -> {
            System.out.println("회원이름 : " + member.getName() + ", 회원나이 : " + member.getAge());
        });

    }


    /**
     *  상태필드 경로 탐색
     *  SELECT m.username, m.age FROM Member m
     *
     *  (단일 값) 연관 경로 탐색 => 내부조인 :: 묵시적 내부조인만 일어남 (inner join)
     *  SELECT m.team FROM Member m
     *  암시적=> select * from member m join team t
     *  
     *  (컬렉션 값) 연관 경로 탐색 => 내부조인 :: 묵시적 내부조인
     *  SELECT t.members FROM Team t
     *
     *  SELECT m FROM Member m
     *  WHERE m.age > (select avg(m2.age) from Member m2)
     *
     *  SELECT m FROM Member m
     *  WHERE (select count(o) from Order o where m = o.member) > 0
     *  SELECT m FROM Member m
     *  WHERE m.orders.size > 0
     *
     *  SELECT m FROM Member m
     *  WHERE EXISTS (SELECT t FROM m.team t where t.name = '좋아요')
     *
     *  // 전체 상품 각각의 제고보다 주문량이 많은 주문들
     *  SELECT o FROM Order o
     *  WHERE o.orderAmount > ALL (select p.stockAmount from product p)
     *
     *  // 어떤 팀이든 소속된 회원
     *  SELECT m FROM Member m
     *  WHERE m.team = ANY (select t from Team t)
     *  
     *  // 20살 이상인 멤버가 소속되어 있는 팀
     *  SELECT t FROM Team t
     *  WHERE t IN (select t2 from Team t2 join t2.members m2 where m2.age >= 20)
     *
     *  // 10살 ~ 20살 멤버를 찾는다.
     *  SELECT m FROM Member m
     *  WHERE m.age BETWEEN 10 AND 20
     *
     *  // 컬렉션 식
     *  // order 주문을 안한 멤버
     *  SELECT m FROM Member m
     *  WHERE m.orders IS NOT EMPTY
     *  
     *  // 컬렉션 식
     *  // memberParam 파라미터가 팀안에 포함되어 있는지
     *  SELECT t FROM Team t
     *  WHERE :memberParam member of t.members
     *
     * 
     *  // CASE 식
     *  1. 기본 CASE
     *  2. 심플 CASE
     *  3. COALESCE
     *  4. NULLIF
     *
     *  1. 기본 CASE
     *  SELECT
     *      CASE
     *          WHEN m.age <= 10 THEN '학생요금'
     *          WHEN m.age >= 60 THEN '경로요금'
     *          ELSE '일반요금'
     *      END AS 요금
     *  FROM Member m
     *
     *  2. 심플 CASE
     *  SELECT
     *      CASE t.name
     *          WHEN '좋아요' THEN '인센티브110%'
     *          WHEN '싫어요' THEN '인센티브120%'
     *          ELSE '인센티브105%'
     *      END AS 월급인상률
     *  FROM Team t
     *
     *  3. COALESCE
     *  // m.username null이면, '이름 없는 회원'을 반환하라
     *  SELECT COALESCE(m.username, '이름 없는 회원') FROM Member m
     *
     *  4. NULLIF
     *  // 사용자 이름이 관리자이면 null을 반환하고 나머지는 본인의 이름을 반환하라
     *  SELECT NULLIF(m.username, '관리자') FROM Member m
     *
     *
     *
     * 
     */

    // 틀 -> 상속 -> 제품

    public static void testQuery(EntityManager em){
        // TODO: 확인하기
//        String sql = "SELECT m FROM Member m "
//                + "LEFT JOIN m.team t "
//                + "ON t.name = 'A'";
//
//        List<com.jpa.demo.queryLanguage.domain2.Member> members = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Member.class)
//                .getResultList();
//        members.stream().forEach(member -> {
//            System.out.println("회원이름 : " + member.getName() + ", 회원나이 : " + member.getAge());
//        });
        // TODO: 확인하기
//        String sql = "SELECT m FROM Member m JOIN FETCH m.team";
//        List<com.jpa.demo.queryLanguage.domain2.Member> members = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Member.class)
//                .getResultList();
//        members.stream().forEach(member -> {
//            em.detach(member);
//            System.out.println("팀이름 : " + member.getTeam().getName());
//            System.out.println("회원이름 : " + member.getName() + ", 회원나이 : " + member.getAge());
//        });
//



        String sql = "SELECT DISTINCT t FROM Team t JOIN t.members WHERE t.name = '좋아요'";
        List<com.jpa.demo.queryLanguage.domain2.Team> teams = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Team.class).getResultList();

        for (com.jpa.demo.queryLanguage.domain2.Team team : teams){
            System.out.println("팀이름 : " + team.getName() + ", team : " + team);

            team.getMembers().stream().forEach(member -> {
                System.out.println("회원이름 : " + member.getName() + ", 회원나이 : " + member.getAge() + ", member : " + member);
            });
        }

    }




    // TODO: 확인해보기
    public static void insertData(EntityManager em){

        Album album = new Album();
        album.setName("앨범");
        album.setArtist("test");
        album.setEtc("to");
        album.setPrice(20000);
        album.setStockQuantity(10);
        em.persist(album);

        Book book = new Book();
        book.setAuthor("저자");
        book.setName("책");
        book.setPrice(10000);
        book.setIsbn("123-4356");
        book.setStockQuantity(5);
        em.persist(book);

        Movie movie = new Movie();
        movie.setName("영화");
        movie.setPrice(40000);
        movie.setActor("배우");
        movie.setDirector("감독");
        movie.setStockQuantity(10);
        em.persist(movie);
    }
    public static void fetchJoin(EntityManager em){
//        String sql = "SELECT t FROM Team t JOIN FETCH t.members WHERE t.name = '좋아요'";
//        List<com.jpa.demo.queryLanguage.domain2.Team> teams = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Team.class).getResultList();
//        teams.stream().forEach(team -> {
//            System.out.println("팀이름 : " + team.getName());
//            // 페치조인으로 인해 지연로딩 발생 안함
//            for (com.jpa.demo.queryLanguage.domain2.Member member : team.getMembers()){
//                System.out.println("멤버이름 : " + member.getName() + ", member : " + member);
//            }
//        });

        String sql = "SELECT m FROM Member m join fetch m.team ORDER BY m.team.name DESC";

        List<com.jpa.demo.queryLanguage.domain2.Member> members = em.createQuery(sql, com.jpa.demo.queryLanguage.domain2.Member.class).getResultList();

        members.stream().forEach(member -> {
            System.out.println("팀이름 : " + member.getTeam().getName());
            System.out.println("이름 : " + member.getName() + ", 나이 : " + member.getAge());
        });

    }

    private static void init2(EntityManager em){
        com.jpa.demo.queryLanguage.domain2.Team teamHate = com.jpa.demo.queryLanguage.domain2.Team.builder()
                .name("싫어요")
                .build();

        com.jpa.demo.queryLanguage.domain2.Team teamLike = com.jpa.demo.queryLanguage.domain2.Team.builder()
                .name("좋아요")
                .build();

        com.jpa.demo.queryLanguage.domain2.Member memberA = com.jpa.demo.queryLanguage.domain2.Member.builder()
                .team(teamHate)
                .age(30)
                .name("MemberA")
                .build();

        com.jpa.demo.queryLanguage.domain2.Member memberB = com.jpa.demo.queryLanguage.domain2.Member.builder()
                .team(teamLike)
                .age(25)
                .name("MemberB")
                .build();


        com.jpa.demo.queryLanguage.domain2.Member memberC = com.jpa.demo.queryLanguage.domain2.Member.builder()
                .team(teamHate)
                .age(23)
                .name("MemberC")
                .build();

        com.jpa.demo.queryLanguage.domain2.Member memberD = com.jpa.demo.queryLanguage.domain2.Member.builder()
                .team(teamLike)
                .age(27)
                .name("MemberD")
                .build();

        com.jpa.demo.queryLanguage.domain2.Member memberE = com.jpa.demo.queryLanguage.domain2.Member.builder()
                .team(teamHate)
                .age(25)
                .name("MemberE")
                .build();

        em.persist(teamHate);
        em.persist(teamLike);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);
    }

    private static void init(EntityManager em){
        Team teamHate = Team.builder()
                .teamName("싫어요")
                .build();

        Team teamLike = Team.builder()
                .teamName("좋아요")
                .build();

        Address address1 = new Address("강남", "언주로", "123-123");
        Address address2 = new Address("경기도", "남양주", "1334-123");

        Address address3 = new Address("부산", "해운대구", "678-123");


        Product productA = Product.builder()
                .name("상품A")
                .price(20000)
                .stockAmount(100)
                .build();

        Order order5 = Order.builder()
                .orderAmount(5)
                .address(address1)
                .product(productA)
                .build();

        Order order10 = Order.builder()
                .orderAmount(10)
                .address(address2)
                .product(productA)
                .build();

        Order order7 = Order.builder()
                .orderAmount(7)
                .address(address3)
                .product(productA)
                .build();

        em.persist(productA);

        em.persist(order7);
        em.persist(order5);
        em.persist(order10);

        Member memberA = Member.builder()
                .age(30)
                .username("kim")
                .team(teamLike)
                .order(order5)
                .build();

        Member memberB = Member.builder()
                .age(25)
                .username("kim")
                .team(teamLike)
                .order(order10)
                .build();

        Member memberC = Member.builder()
                .age(21)
                .username("lee")
                .team(teamHate)
                .order(order7)
                .build();

        Member memberD = Member.builder()
                .age(50)
                .username("park")
                .team(teamHate)
                .order(order10)
                .build();

        Member memberE = Member.builder()
                .age(30)
                .username("hong")
                .team(teamLike)
                .order(order7)
                .build();


        em.persist(teamLike);
        em.persist(teamHate);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);
    }

}
