# July08Application 학습 정리

## 일시
- 2026년 07월 08일 (수)

## 학습 내용 요약

### 1. app 모듈 (Activity 간 데이터 전달 및 결과 수신)
- **`ActivityResultLauncher` 사용**: `startActivityForResult` 대신 현대적인 `registerForActivityResult`를 사용하여 액티비티 전환 및 결과를 수신하는 방법을 학습했습니다.
- **Explicit Intent**: `Intent`를 통해 데이터를 전달하고, 다른 액티비티(`CalcActivity`)에서 계산 결과를 다시 받아오는 로직을 구현했습니다.
- **계산기 로직**: 두 수와 연산자를 전달받아 처리하는 기능을 구현했습니다.

### 2. picker 모듈 (위젯형 피커 활용)
- **`DatePicker` & `TimePicker`**: 레이아웃 내에 포함된 위젯 형태의 피커를 사용하는 방법을 학습했습니다.
- **동적 가시성 제어**: 버튼 클릭에 따라 `visibility` 속성을 변경하여 날짜 피커와 시간 피커를 번갈아 가며 보여주는 기능을 구현했습니다.

### 3. pickerdiolog 모듈 (대화상자형 피커 및 알림창)
- **`DatePickerDialog` & `TimePickerDialog`**: 팝업 형태의 대화상자를 통해 날짜와 시간을 선택받는 기능을 구현했습니다.
- **`AlertDialog`**: 사용자에게 알림 메시지를 보여주거나 확인/취소 등의 선택을 받는 기본적인 대화상자 활용법을 학습했습니다.
- **ViewBinding 활용**: XML 레이아웃의 요소들에 안전하게 접근하는 방식을 적용했습니다.
