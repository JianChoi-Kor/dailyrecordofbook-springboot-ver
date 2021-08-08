# project-dailyrecordofbook (대구 독서 커뮤니티 웹사이트)
개인 프로젝트 / 책방일지 (배포 완료)

[책방일지 사이트 바로가기](http://dailyrecordofbook.com)

<br>
<br>

## 1. 사이트 설명 및 사용 기술
> 독서 커뮤니티 홍보 및 활성화를 위한 사이트입니다.
> 기존 spring으로 개발하다가 취업 후 spring boot를 사용하게 되면서 spring boot 기술을 조금이라도 더 익히기 위해 spring boot로 새로 개발했습니다.
> 
> 기본적인 게시판의 기능을 활용한 사이트입니다.
> 프론트는 thymeleaf template을 사용했습니다.
> oauth2을 사용하여 소셜 로그인을 추가했습니다.
> 제대로 사용할 부분이 없어서 비중이 크진 않지만 JPA, QueryDsl를 사용했습니다.
> 회원가입 과정에서는 이메일 인증이 있어 javax.mail을 사용하였습니다.
> 글쓰기 에디터는 CKEditor를 사용했으며, 업로드되는 이미지 사이즈가 기준보다 클 경우 기준 사이즈로 사이즈를 변경하여 저장하였습니다.
> 

<br>
<br>


## 2. 배포 후기 및 아쉬운 점
> 프로젝트 생성부터 프론트엔드, 백앤드 개발 후에는 호스팅, 배포, 도메인 설정까지 혼자 해보면서 다양한 경험치를 쌓은 것 같아 뿌듯합니다.
> 
>
> security를 사용했지만 소셜 로그인 외에 부분에서 활용을 하지 못하여 security를 제대로 사용하지 못한 것 같아 아쉽습니다.
> spring boot로 새로 개발하며 프론트 부분에서 걸리는 시간을 조금이라도 단축하기 위해 기존 spring으로 만들던 html, js, css를 활용했는데, 비효율적이고 중복된 부분이 너무 많은 점이 아쉽습니다.
> 초반에는 git의 사용법을 잘 몰라 브런치도 없이 작업하였는데 다음 작업부터는 혼자 하더라도 각각의 브런치를 나눠 효율적으로 작업할 수 있을 것 같습니다.
> 배포는 했지만 아직 내부적으로 부족한 부분이 많습니다. 중간부터 손을 대면 수정할 부분이 많을 것 같아서 사용할 수 있는 기술을 사용하지 못한 부분도 많습니다.
> 계속 배우면서 추가할 부분은 추가하고 수정할 부분은 수정하여 더욱 잘 동작하는 웹사이트를 만들겠습니다.
> 리펙토링을 하게 된다면 리펙토링보다 실제로 일하면서 사용했던 스킬들을 가지고 처음부터 조금 더 효율적으로 개발해보고 싶습니다.

<br>
<br>
