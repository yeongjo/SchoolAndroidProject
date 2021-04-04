# 총알 피하기

2016180024 송영조



## 게임 컨셉

화면에 플레이어가 보여집니다.

터치한 상태로 드래그하여 플레이어가 다음에 이동할곳을 정할 수 있습니다.

이동할 곳을 정하고 터치를 때면 그곳으로 플레이어가 무적상태로 빠르게 이동합니다.

무적상태에 부딫힌 총알들은 사라집니다.

플레이어를 조종해서 천천히 날아오는 총알들을 피하면 됩니다.

오랫동안 살아남을수록 점수가 올라갑니다. 점수는 오른쪽 위에 표시됩니다.

점수가 100점이상으로 오르면 2번째 스테이지, 200점 이상으로 오르면 3번째 스테이지가 되며 새로운 종류의 총알이 나옵니다.

플레이어가 총알에 맞으면 게임이 멈추며 현재 점수가 팝업으로 점수가 표시되고 그 팝업에는 다시하기 버튼이 표시됩니다.

![image-20210404053700780](https://user-images.githubusercontent.com/26712352/113496864-dfc6ff80-9538-11eb-9026-eb0908c26e7f.png)


아래 이미지는 플레이어가 드래그로 새로 이동할 지점을 선택하고 터치를 때면 그 위치로 이동하기 전 드래그 중인 상황을 표현한 이미지입니다.

![image-20210404100638097](https://user-images.githubusercontent.com/26712352/113496866-e0f82c80-9538-11eb-9b1c-36a869921b18.png)



## 하이컨셉

![image-20210404111928392](https://user-images.githubusercontent.com/26712352/113496867-e2295980-9538-11eb-8780-7efbef5c2dac.png)
![image](https://user-images.githubusercontent.com/26712352/113496918-7abfd980-9539-11eb-9790-f58ad490d456.png)


## 개발 범위

터치 후 드래그 하여 목표 위치을 정하면 Path와 목표지점 표시 이미지를 화면에 그리는걸 구현합니다.

터치를 때면 플레이어가 목표지점까지 천천히 이동하는 것을 구현합니다.

방향이 정해진 총알, 플레이어를 향해 천천히 휘어지며 날아가는 총알, 총알을 발사하는 총알을 구현합니다.

총알과 플레이어의 충돌을 구현합니다.

목표지점까지 이동중에는 충돌시 피해를 받지않도록 구현합니다.

충돌시 게임이 멈추고 점수를 표시하게 하며 재시작을 물어보는 팝업이 뜨게 합니다.

점수가 100단위로 오를때마다 스테이지가 증가하게 만들며, 스테이지가 증가될때마다 다른 종류의 총알이 나오도록 구현합니다.
