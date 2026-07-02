# 2024년 7월 2일 안드로이드 학습 정리

본 프로젝트는 2024년 7월 2일 안드로이드 레이아웃 기초 및 실습 내용을 담고 있습니다.

## 📅 학습 날짜
- **일시**: 2024년 7월 2일
- **주제**: 안드로이드 레이아웃(Layout)의 종류와 활용

---

## 📂 모듈별 학습 내용

### 1. `app` (LinearLayout 기초)
- `LinearLayout`의 기본적인 사용법 학습.
- `orientation` (vertical, horizontal) 속성을 통한 뷰 배치.
- `layout_weight` 속성을 이용한 화면 비율 분할 실습.

### 2. `gravityexam` (Gravity 속성 이해)
- `android:gravity`: 뷰 내부의 내용물(텍스트 등)의 정렬 방식 제어.
- `android:layout_gravity`: 부모 레이아웃 내에서의 뷰 자체의 정렬 방식 제어.
- 두 속성의 차이점을 실습을 통해 이해.

### 3. `relativelayout` (RelativeLayout 활용)
- 부모 컨테이너나 다른 위젯과의 상대적인 위치를 기준으로 배치하는 `RelativeLayout` 학습.
- `layout_centerInParent`, `layout_alignParentTop`, `layout_above`, `layout_toStartOf` 등 주요 속성 활용.

### 4. `dogcatchoiceshow` (FrameLayout 및 Visibility 제어)
- `FrameLayout`을 이용한 뷰 겹치기 실습.
- Kotlin 코드에서 `View.visibility` (`VISIBLE`, `INVISIBLE`) 속성을 사용하여 버튼 클릭 시 이미지를 교체하여 보여주는 로직 구현.

### 5. `phonepad` (복합 레이아웃 실습)
- `LinearLayout`의 중첩(Nesting)과 `layout_weight`를 활용하여 실제 전화번호 패드(Keypad) 형태의 UI 구성.
- `MaterialButton`과 스타일(Style)을 적용한 일관된 UI 디자인 실습.

---

## 💡 주요 개념 요약
- **LinearLayout**: 선형 배치 (가로/세로).
- **RelativeLayout**: 상대적 위치 배치.
- **FrameLayout**: 겹쳐서 배치 (가장 나중에 추가된 뷰가 위로 올라옴).
- **Weight**: 여유 공간을 비율에 따라 배분.
- **Visibility**: 뷰의 노출 여부 제어.
