# TossApplication (토스증권 클론 프로젝트)

안드로이드 기반의 현대적인 금융 UI/UX를 구현한 토스증권 클론 애플리케이션입니다. 고도화된 레이아웃 최적화와 실시간 데이터 연동 시뮬레이션을 통해 실제 서비스 수준의 사용성을 제공합니다.

## 🚀 주요 기술 스택 및 아키텍처
- **Language**: Kotlin 2.x
- **Build System**: Android Gradle Plugin (AGP) 9.2.1 (Built-in Kotlin Support)
- **Architecture**: MVVM (ViewModel + StateFlow), Repository Pattern, ViewBinding
- **UI Framework**: Material Components, ConstraintLayout (Flat Hierarchy)
- **Data Persistence**: SQLite (Room-ready implementation)

## 💾 데이터 관리 및 처리 (Data Layer)
애플리케이션의 데이터 흐름은 안정성과 실시간성을 확보하기 위해 계층화되어 있습니다.

### 1. 로컬 데이터베이스 (Persistent Storage)
- **SQLite**: `AppDbHelper`를 통해 `TossStock.db` 파일에 사용자 보유 자산 정보를 영구 저장합니다.
- **Contract & Entry**: `StockContract`를 정의하여 데이터베이스 스키마(종목 코드, 이름, 매수 평단가, 보유 수량 등)를 엄격하게 관리합니다.
- **Repository Pattern**: `StockRepository`가 데이터 소스를 추상화하여 ViewModel에 필요한 도메인 모델(`StockModel`)을 공급합니다.

### 2. 실시간 시뮬레이션 (Mock Data Stream)
- **ViewModel State**: `HomeViewModel`과 `StockDetailViewModel`에서 `StateFlow`를 사용하여 UI 상태를 관리합니다.
- **Real-time Updates**: `Timer` 및 `Coroutine`을 활용하여 2초 간격으로 주식 시세 등락을 무작위로 생성, 실시간 트레이딩 환경을 시뮬레이션합니다.

## 📱 화면 및 프래그먼트 구성 (Presentation Layer)
메인 화면은 단일 액티비티 프레임워크 내에서 4개의 주요 탭 프래그먼트가 교체되는 구조입니다.

### 1. 메인 내비게이션 (`MainActivity`)
- **Bottom Navigation**: 화면 최하단에 고정된 스티키 탭바를 통해 4대 핵심 서비스로 즉시 전환합니다.
- **Fragment Management**: `supportFragmentManager`를 사용하여 백스택을 관리하며 탭 전환 시 메모리 효율성을 고려한 인젝션 구조를 가집니다.

### 2. 핵심 프래그먼트 (Fragments)
- **홈 (`HomeFragment`)**: 종합 자산 요약, 예수금 카드, 국내/해외 보유 주식 리스트를 렌더링하는 마스터 대시보드입니다.
- **관심 (`WatchFragment`)**: 사용자 지정 관심 종목 그룹 관리 및 AI 매칭 실시간 뉴스 피드를 제공합니다.
- **발견 (`DiscoveryFragment`)**: 실시간 거래대금 랭킹, 테마별 카테고리 칩셋, 커뮤니티 추천 카드를 포함한 시장 탐색 화면입니다.
- **피드 (`FeedFragment`)**: 투자자 커뮤니티 타임라인으로, 좋아요/팔로우 기능이 포함된 소셜 인터랙션 공간입니다.

### 3. 주요 전용 액티비티 (Activities)
- **인증**: `AuthActivity`(생체 인식), `PasswordActivity`(커스텀 셔플 키패드)
- **계좌/투자**: `AccountActivity`(상세 잔액), `InvestmentDetailActivity`(포트폴리오 분석), `AssetProportionActivity`(도넛 차트 비중)
- **트레이딩**: `StockDetailActivity`(호가/차트), `OrderBuyActivity`(매수), `OrderSellActivity`(매도)
- **유틸리티**: `SearchActivity`(통합 검색), `AiSignalActivity`(제미나이 AI 정보), `StockAccumulateActivity`(정기 적립)

## 🛠 레이아웃 설계 원칙
- **Flat View Hierarchy**: `ConstraintLayout`을 100% 활용하여 뷰 계층을 평탄화함으로써 **Overdraw**를 최소화하고 렌더링 성능을 극대화했습니다.
- **Sticky UI**: 하단 주문 버튼, 상단 액션바 등을 스크롤 영역에서 분리하여 사용자 조작 편의성을 확보했습니다.
- **Responsive Design**: `NestedScrollView`를 적용하여 다양한 크기의 콘텐츠가 포함된 금융 리포트도 끊김 없이 부드럽게 스크롤됩니다.

## 📂 프로젝트 구조
```
kr.hnu.ice.tossapplication
├── data              # SQLite DB, Contract, Repository, Data Models
├── view              # Activities (Auth, Trade, Account, etc.)
├── fragment          # Fragments (Home, Watch, Discover, Feed)
├── viewmodel         # UI State Management (StateFlow)
├── adapter           # RecyclerView Adapters (DiffUtil based)
└── databinding       # ViewBinding generated classes
```
