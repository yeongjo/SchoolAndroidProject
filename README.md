# 피하기

2016180024 송영조



## 게임소개

드래그 한뒤 떼서 움직이고, 움직이는 중엔 주위를 공격합니다.



**[게임플레이 영상]**

[![youtubeGamePlayVideo](https://img.youtube.com/vi/J6eX6YOg_wM/0.jpg)](https://youtu.be/J6eX6YOg_wM)





## 개발 계획

개발진척도 (200%)

만드는게 재밌어서 원래 계획에 없던
체력, 스테미나, 경험치, 다양한 효과의 아이템, 이펙트, 리더보드 기능을 추가했습니다.

![image](https://user-images.githubusercontent.com/26712352/121518614-a674a980-ca2b-11eb-94a6-9e1eb0b28fa6.png)



## 사용한 기술

타이틀 씬, 게임플레이 씬 이동

파이어베이스 리더보드 연동

MediaPlayer bgm재생

back키 연동

홈키 눌러 나갔다 들어올때 노래 및 게임 일시정지



## 수업시간에서 차용한 것

AlertDialog 사용하여 다시시작 여부, 아이템 선택 창 구현

Scene 구현

SoundPool 구현

Level 텍스트 출력

VerticalBackground

string.xml 한국어, 영어 지원



## 직접 개발한 것

파이어베이스 연동 리더보드

CircleCollider

Item [습득시 효과발동, 움직이는 중 효과발동, 공격했을때 효과발동]

Sound에 MediaPlayer Pool 기능 추가

Vector2 클래스에 회전, 사칙연산, 정규화, 거리 등 유틸리티 함수 구현
[플레이어 이동시 주변 전기효과 구현에 큰 도움]





## 아쉬움

졸업작품과 타과목 과제에 의해 출시 가능한 정도까지 개발하지 못했습니다.

리더보드 방식의 재미 한계를 극복할 방법을 생각해내지 못했습니다.
단순한 리더보드만으론 당장의 플레이 동기를 만들어주지 않았습니다.



## 지인 인터뷰

[![youtubeGamePlayVideo](https://img.youtube.com/vi/emGGLUzXiLs/0.jpg)](https://youtu.be/emGGLUzXiLs)



































# 중간발표

중간발표 영상

[![youtubeGamePlayVideo](https://img.youtube.com/vi/X-H76QQgT8w/0.jpg)](https://youtu.be/X-H76QQgT8w)



## 게임 소개

![ezgif-7-7e35bd3f60f7](https://user-images.githubusercontent.com/26712352/118391685-98bc4600-b670-11eb-94ab-bccaf7a6a369.gif)

매 레벨마다 새로운 아이템을 선택합니다.

드래그를 통해 움직이며, 움직이는동안 부딫힌 총알은 사라집니다.

움직일때는 스테미나가 소비되며 적과 충돌하면 체력이 소모됩니다.



## 진행 상황 [80%]

**[완료]** 플레이어 이동/ 데미지/ 체력, 스테미나 바 

**[완료]** 게임 재시작, 아이템 습득 UI

**[완료]** 점수, 레벨 UI

**[완료]** 파이어베이스 랭크보드 연동

사운드

리펙토링

그래픽 리소스 변경



## 깃 커밋

![image](https://user-images.githubusercontent.com/26712352/118392099-aecb0600-b672-11eb-8fee-b00cb9ed9f97.png)



## 코드 구조

**MainGame** - 모든 오브젝트를 렌더, 업데이트하고 충돌로직을 처리합니다. 플레이어 사망시 재시작 다이어로그를 띄웁니다.

**Vector2** - 회전, 각도, 거리, 정규화등 여러 연산들을 편하게 할 수 있게 돕는 클래스입니다.

**RankBoard** - TextView를 화면에 덧그려 순위를 표시합니다, 파이어베이스에서 순위표를 받아오고 플레이어의 점수를 업로드합니다. 로컬 데이터에 최근에 사용한 이름을 저장합니다.

**Player** : GameObject, CircleCollidable

- 드래그 관련 로직
  - 드래그 하여 이동
  - 스테미나 소비
  - 드래그 시작위치와 종료 위치 표시를 범위로 표시
- 데미지[+이펙트], 체력, 죽었는지 관리

**PlayerHUD** - 플레이어의 체력, 스테미나를 표시해줍니다.

**EnemyGenerator** - 적 3마리당 레벨을 1씩 올리고, 레벨이 올라갈때마다 플레이어에게 랜덤 아이템을 고를 수 있는 창의 띄워주는 함수를 호출하며, 챕터가 오를때마다 난이도 증가 공식에 따라 적 난이도를 올립니다.

**CircleCollidable** - getCollider()를 통해 콜라이더를 얻어오는 함수가 있는 인터페이스입니다.

**CircleCollider** - 위치, 반지름을 가진 충돌영역 정보입니다.

**Enemy** - 방향 따라 날아가는 적입니다.

**FollowEnemy** : Enemy - 플레이어를 향해 휘는 적입니다.

**LaserEnemy** : Enemy - 플레이어를 조준했다 잠시뒤에 빠르게 날아오는 적입니다.

**ParentEnemy** : Enemy - 일정시간 뒤에 다른 적들을 주변에 소환합니다.

**RandomMoveEnemy** : Enemy - 움직였다 멈췄다하는 적입니다.

**Item** - 상태 별로 호출될수 있는 기본 함수 및 이름, 양에 관한 단위 텍스트 함수(a lot, a litte)가 정의된 클래스입니다.

**AttackRangeItem** : Item - 플레이어가 소지하면 공격시 공격 범위가 늘어나는 클래스입니다.

**StatsItem** : Item - 회복, 체력증가, 스테미나증가, 크기증가, 속도증가 등 스탯을 조절하는 클래스입니다.

**LifeStealItem** : Item - 공격시 체력회복이 정의된 클래스입니다.

**VerticalScrollBackground** - 세로 스크롤을 담당합니다.

































# 기획발표

기획발표 영상

[![youtubeGamePlayVideo](https://img.youtube.com/vi/X7D-IGz1E3Y/0.jpg)](https://youtu.be/X7D-IGz1E3Y)



## 게임 컨셉

화면에 플레이어가 보여집니다.

터치한 상태로 드래그하여 플레이어가 다음에 이동할곳을 정할 수 있습니다.

아래 이미지는 이동하기 전 플레이어입니다
![image](https://user-images.githubusercontent.com/26712352/113496935-ac38a500-9539-11eb-86e1-3228c2d97fc2.png)

아래 이미지는 플레이어가 드래그로 새로 이동할 지점을 선택한 이미지입니다.
![image-20210404100638097](https://user-images.githubusercontent.com/26712352/113496866-e0f82c80-9538-11eb-9b1c-36a869921b18.png)

아래 이미지는 드래그가 끝나고 터치를 때서 그 위치로 이동된 플레이어를 나타냈습니다. 드래그로 이동할곳을 정해 터치를 때면 그곳으로 플레이어가 무적상태로 천천히 이동합니다.
![image](https://user-images.githubusercontent.com/26712352/113496918-7abfd980-9539-11eb-9790-f58ad490d456.png)

무적상태에서 플레이어와 부딫힌 총알들은 사라집니다.

플레이어를 조종해서 천천히 날아오는 총알들을 피하면 됩니다.

오랫동안 살아남을수록 점수가 올라갑니다. 점수는 오른쪽 위에 표시됩니다.

점수가 100점이상으로 오르면 2번째 스테이지, 200점 이상으로 오르면 3번째 스테이지가 되며 새로운 종류의 총알이 나옵니다.

플레이어가 총알에 맞으면 게임이 멈추며 현재 점수가 팝업으로 점수가 표시되고 그 팝업에는 다시하기 버튼이 표시됩니다.



## 하이컨셉

![image-20210404111928392](https://user-images.githubusercontent.com/26712352/113496867-e2295980-9538-11eb-8780-7efbef5c2dac.png)


## 개발 범위

터치 후 드래그 하여 목표 위치을 정하면 Path와 목표지점 표시 이미지를 화면에 그리는걸 구현합니다.

터치를 때면 플레이어가 목표지점까지 천천히 이동하는 것을 구현합니다.

총알을 발사하고 발사할 총알 종류를 결정하며 몇초 간격으로 발사하며 관리할 클래스를 만듭니다.

방향이 정해진 총알, 플레이어를 향해 천천히 휘어지며 날아가는 총알, 총알을 발사하는 총알을 구현합니다.

총알과 플레이어의 충돌을 구현합니다.

목표지점까지 이동중에는 충돌시 피해를 받지않도록 구현합니다.

충돌시 게임이 멈추고 점수를 표시하게 하며 재시작을 물어보는 팝업이 뜨게 합니다.

점수가 100단위로 오를때마다 스테이지가 증가하게 만들며, 스테이지가 증가될때마다 다른 종류의 총알이 나오도록 구현합니다.



## 개발 일정

4/5 1주차 - 터치 후 드래그하여 목표지점에 이미지를 그리고 기존위치와 목표위치 사이에 이동될 위치를 직선으로 그립니다.

2주차 - 터치를 때면 플레이어가 기존위치에서 목표위치로 천천히 이동하는 것을 구현합니다. 총알을 발사하고 발사할 총알 종류를 결정하며 몇초 간격으로 발사하며 관리할 클래스를 만듭니다.

3주차 - 방향이 정해진 총알을 구현합니다. 플레이어를 향해 천천히 휘어지는 총알을 구현합니다. 총알을 발사하는 총알을 구현합니다.

4주차 - 플레이어와 총알의 충돌을 구현하며 충돌시 총알과 플레이어를 파괴하고 플레이어는 죽었다고 표시합니다.

5주차 - 게임이 진행될때 점수를 계속 증가하게하며, 충돌시 게임이 멈추고 점수를 표시하게하며 재시작을 물어보는 팝업이 뜨게 합니다.

6주차 - 플레이어가 목표지점까지 이동 중에는 총알과 충돌시에 총알만 사라지게하고 플레이어는 피해를 입지않게 합니다.

7주차 - 점수가 100점 단위로 오를때마다 스테이지가 증가하게하며, 스테이지가 증가될 때마다 다른 종류의 총알이 나오도록 구현합니다.

8주차 - 총알과 플레이어 파괴, 플레이어 이동, 배경음악 소리를 추가합니다.

6/4 9주차 - 플레이어를 프레임 애니메이션으로 변경합니다. 코드를 점검받고 문제가 있다면 수정하기 위한 여분의 시간입니다.
