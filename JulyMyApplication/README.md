# JulyMyApplication

## 📅 학습 내용 (7월 6일)

어제(7월 6일) 진행한 주요 학습 및 구현 내용은 다음과 같습니다.

### 1. 스톱워치(Stopwatch) 기능 구현
- `Chronometer` 위젯을 사용하여 시간을 측정하는 기능을 구현했습니다.
- **시작(Start)**, **중지(Stop)**, **초기화(Reset)** 기능을 각각 버튼에 연결했습니다.
- `SystemClock.elapsedRealtime()`을 활용하여 정확한 시간 계산 및 중지 시 시점 저장(`elapsedtime`)을 처리했습니다.

### 2. 뷰 바인딩(View Binding) 적용
- `ActivityMainBinding`을 사용하여 레이아웃 리소스에 안전하게 접근하도록 구현했습니다.
- `build.gradle.kts`에서 `viewBinding` 설정을 활성화했습니다.

### 3. 사용자 경험(UX) 개선
- 버튼의 상태(`isEnabled`)를 제어하여 논리적인 흐름(예: 시작 중에는 시작 버튼 비활성화)을 관리했습니다.
- `Toast`를 사용하여 중지 시 경과 시간을 사용자에게 알렸습니다.

### 4. 뒤로가기 버튼 처리 (`onBackPressedDispatcher`)
- `addCallback`을 사용하여 뒤로가기 버튼 클릭 시 바로 종료되지 않고, **5초 이내에 다시 눌러야 종료**되도록 구현하여 실수로 앱이 종료되는 것을 방지했습니다.

### 5. 화면 방향 전환 대응
- 가로 모드(`layout-land`) 레이아웃을 별도로 구성하여 화면 회전 시에도 UI가 깨지지 않도록 대응했습니다.
- `AndroidManifest.xml`에서 `windowSoftInputMode` 설정을 확인하고 관리했습니다.
