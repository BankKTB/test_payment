export class Constant {
  public LIST_AREA_CODE = [];
  public PER_PAGINATION = 5;
  public PAYMENT_METHOD = [];
  public PAYMENT_METHOD_SEARCH = [];

  public PAYMENT_SPECIAL_GL = [];
  public PAYMENT_SPECIAL_GL_SEARCH = [];

  public DOC_TYPE = [];
  public DOC_TYPE_SEARCH = [];

  public WIDTH_DIALOG_VARIANT = '50vw';
  public PANEL_CLASS_DIALOG_MASTER = 'dialog-search-master';

  // header dialog-search
  public HEADER_DIALOG_SEARCH = {
    companyCode: ['เลือก', 'รหัสบริษัท', 'ชื่อ'],
    areaCode: ['เลือก', 'รหัสพื้นที่', 'ชื่อ', 'คำอธิบาย'],
    disbursementCode: ['เลือก', 'หน่วยเบิกจ่าย', 'ชื่อ', 'คำอธิบาย'],
    vendorTaxId: ['เลือก', 'รหัสประจำตัวผู้เสียภาษี', 'รหัสผู้ขาย', 'ชื่อผู้ขาย'],
  };

  // Sidebar in config
  public listSidebar = [
    { id: '1', name: 'รหัสบริษัท', link: '/companyPayee', active: 'true' },
    { id: '2', name: 'รหัสบริษัทที่ชำระเงิน', link: '/companyPaying', active: 'true' },
    { id: '3', name: 'วิธีการชำระเงิน/ประเทศ', link: '/payMethodComponent', active: 'true' },
    {
      id: '4',
      name: 'การปรับปรุงข้อมูลรหัสบริษัทสำหรับวิธีการชำระเงิน',
      link: '/configUpdateDataComponent',
      active: 'true',
    },
    { id: '5', name: 'ลำดับตามการจัดลำดับ', link: '/configSequenceComponent', active: 'true' },
    { id: '6', name: 'บัญชีธนาคาร', link: '/configBankAccountComponent', active: 'true' },
    {
      id: '7',
      name: 'จำนวนเงินที่เหลืออยู่',
      link: '/configAmountRemainingComponent',
      active: 'true',
    },
  ];

  public LIST_TABLE_OM = new Map([
    ['documentType', { name: 'ประเภทเอกสาร', seq: 13 }],
    // ['compCode', { name: 'หน่วยงาน', seq: 0 }],
    ['dateDoc', { name: 'วันที่เอกสาร', seq: 19 }],
    ['dateAcct', { name: 'วันที่ผ่านรายการ', seq: 20 }],
    ['amount', { name: 'จำนวนเงิน', seq: 25 }],
    ['invoiceDocumentNo', { name: 'ใบกำกับสินค้าอ้างอิง', seq: 15 }],
    ['originalDocumentNo', { name: 'เลขที่เอกสาร', seq: 14 }],
    ['originalFiscalYear', { name: 'ปีบัญชี', seq: 16 }],
    ['paymentMethod', { name: 'วิธีชำระเงิน', seq: 27 }],
    ['headerReference', { name: 'การอ้างอิง', seq: 18 }],
    ['userPost', { name: 'ผู้ใช้', seq: 34 }],
    ['fiArea', { name: 'รหัสจังหวัด', seq: 31 }],
    ['costCenter', { name: 'ศูนย์ต้นทุน', seq: 36 }],

    ['fundSource', { name: 'แหล่งของเงิน', seq: 38 }],
    ['bgCode', { name: 'รหัสงบประมาณ', seq: 37 }],
    ['assignment', { name: 'การกำหนด', seq: 24 }],

    ['brLine', { name: 'บรรทัดรายการ', seq: 17 }],
    ['subAccount', { name: 'บัญชีย่อย', seq: 40 }],
    ['subAccountOwner', { name: 'เจ้าของบัญชีย่อย', seq: 41 }],

    ['depositAccount', { name: 'บัญชีเงินฝาก', seq: 33 }],
    ['depositAccountOwner', { name: 'เจ้าของบัญชีเงินฝาก', seq: 32 }],
    ['lineItemText', { name: 'ข้อความ', seq: 26 }],
    ['specialGL', { name: 'แยกประเภทพิเศษ', seq: 28 }],
    ['dateBaseLine', { name: 'วันที่เป็นฐานการจ่าย', seq: 30 }],
    ['reference2', { name: 'คีย์อ้างอิง 2', seq: 29 }],
    ['poDocumentNo', { name: 'เอกสารการจัดซื้อ', seq: 21 }],
    ///
    ['vendorName', { name: 'ชื่อ 1', seq: 22 }],
    ['vendorCode', { name: 'ผู้ขาย', seq: 42 }],
    ['bankKey', { name: 'คีย์ธนาคาร', seq: 43 }],
    ['bankAccountNo', { name: 'เลขที่บัญชีธนาคาร', seq: 44 }],
    ['bankAccountHolderName', { name: 'ผู้ถือบัญชี', seq: 45 }],
    ['fundType', { name: 'ประเภทกองทุน', seq: 39 }],
    ///

    ['approve', { name: 'อนุมัติ', seq: 2 }],
    ['notApprove', { name: 'ไม่อนุมัติ', seq: 3 }],
    ['confirmSeller', { name: 'การยืนยันผู้ขาย', seq: 4 }],
    ['bot', { name: 'BOT', seq: 5 }],
    ['reason', { name: 'Reason', seq: 6 }],
    ['iconStatus', { name: 'สถานะ', seq: 8 }],
    ['frStatus', { name: 'FR Status', seq: 9 }],

    ['wareHouseNo', { name: 'เลขที่คลังรับ', seq: 7 }],
    ['textStatus', { name: 'สถานะ', seq: 10 }],
    ['textStatus', { name: 'สถานะ', seq: 10 }],
    ['errWtInfo', { name: 'Err WtInfo', seq: 11 }],
    ['deduct', { name: 'หัก', seq: 12 }],
    ['partnerBankType', { name: 'ประเภทธนาคารคู่ค้า', seq: 23 }],
    ['depositMoney', { name: 'ปรับเงินฝาก', seq: 35 }],
  ]);

  public LIST_TABLE_DETAIL_DOCUMENT = new Map([
    ['postingKey', { name: 'PK', seq: 1 }],

    ['glAccount', { name: 'บัญชี', seq: 2 }],
    ['lineDescription', { name: ' ข้อความแบบสั้นทาง บ/ช', seq: 3 }],

    ['amount', { name: ' จำนวนเงิน', seq: 4 }],
    ['costCenter', { name: ' ศ.ต้นทุน', seq: 5 }],
    ['paymentCenter', { name: ' หน่วย บ/จ', seq: 6 }],
    ['reference1', { name: ' Ref.ke', seq: 6 }],
  ]);

  public LIST_TYPE_SEARCH_DOCTYPE_KB = new Map([
    ['K0', 'K0 - เงินจ่ายสิ้นเดือน'],
    ['K1', 'K1 - ลูกหนี้เงินยืม'],
    ['K8', 'K8 - เงินอุดหนุน'],
    ['KC', 'KC - ขอเบิก (I.ข)'],
    ['KD', 'KD - ขอเบิก(1.ข) กันเงิน'],
    ['KE', 'KE - ขอเบิก(2.ข)'],
    ['KF', 'KF - ขอเบิก(2.ข) กันเงิน'],
    ['KL', 'KL - ใบสำคัญงปม(2)'],
    ['KM', 'KM - ใบสำคัญงปม(2) กันเงิน'],
    ['KH', 'KH - ขอเบิก(J.ข)'],
    ['KA', 'KA - ขอเบิก(1.ก)'],
    ['K6', 'K6 - ส่งเกินจ่ายคืน(ถอนคืนรายได้)'],
    ['KI', 'KI - ขอเบิก(4.ข)'],
    ['KQ', 'KQ - ขอเบิกเงินกู้นอกงบ(4'],
    ['KS', 'KS - ขอเบิกเงินกู้นอกงบ(3ข)'],
    ['KB', 'KB - ขอเบิก(1.ก) กันเงิน'],
    ['KG', 'KG - ขอเบิก(3.ก)'],
    ['KN', 'KN - ใบสำคัญนอกงปม.(4)'],
    ['K5', 'K5 - เงินดาวน์'],
    ['KX', 'KX - การลดหนี้'],
    ['KY', 'KY - ค้างรับ/ค้างจ่าย'],
    ['JV', 'JV - บันทึกรายการบัญชีทั่วไปไม่เกี่ยวกับเงินสดและเทียบเท่าเงินสด'],
    ['J0', 'J0 - ปรับเงินฝากคลัง-Auto'],
    ['KZ', 'KZ - กลับรายการขอเบิก'],
    ['PM', 'PM - จ่ายเงิน Manual'],
    ['BG', 'BG - พัก-ระบบงบประมาณ'],
  ]);

  public LIST_REASON_BACK_LIST = [
    { id: '', name: '---- กรุณาระบุเหตุผลในการกลับรายการ ----' },
    { id: '01', name: '01 - การกลับรายการเนื่องจากรหัสบัญชีผิด' },
    { id: '02', name: '02 - การกลับรายการเนื่องจากจำนวนเงินผิด' },
    { id: '03', name: '03 - การกลับรายการเนื่องจากรหัสอื่นๆผิด' },
    { id: '04', name: '04 - การกลับรายการเนื่องจากผิดจากหลายสาเหตุ' },
    { id: '05', name: '05 - การกลับรายการค้างรับ/ค้างจ่าย' },
    { id: '06', name: '06 - การกลับรายการเมื่อปิดงวด' },
    { id: '07', name: '07 - การกลับรายการขอเบิกที่ไม่ผ่านการอนุมัติ' },
  ];

  public MONTH_FULL_TH = [
    { month: '1', name: 'มกราคม' },
    { month: '2', name: 'กุมภาพันธ์' },
    { month: '3', name: 'มีนาคม' },
    { month: '4', name: 'เมษายน' },
    { month: '5', name: 'พฤษภาคม' },
    { month: '6', name: 'มิถุนายน' },
    { month: '7', name: 'กรกฎาคม' },
    { month: '8', name: 'สิงหาคม' },
    { month: '9', name: 'กันยายน' },
    { month: '10', name: 'ตุลาคม' },
    { month: '11', name: 'พฤศจิกายน' },
    { month: '12', name: 'ธันวาคม' },
  ];

  public LIST_PAYMENT_BLOCK = [
    { id: '0', name: '0 - รออนุมัติขั้น1ในสรก.' },
    { id: 'A', name: 'A - รออนุมัติขั้น2ในสรก.' },
    { id: 'B', name: 'B - ระงับการชำระเงิน' },
    { id: ' ', name: '  - ชำระเงินได้' },
    { id: 'E', name: 'E - PTO ไม่อนุมัติ' },
    { id: 'F', name: 'F - เลขที่บัญชีผิดพลาด' },
    { id: 'N', name: 'N - ไม่อนุมัติภายใน สรก.' },
    { id: 'P', name: 'P - รอ CGD อนุมัติ' },
  ];

  public LIST_REFERENCE1 = ['KTB', 'OTH1', 'OTH2', 'SWIFT'];

  public LIST_TYPE_TAX = [
    { id: 'ZVAT', name: 'ภาษีจากการจัดซื้อ(บาท)' },
    { id: 'ZVA2', name: 'ภาษีจากการจัดซื้อ(%)' },
  ];
  public LIST_TYPE_GENERAL_TEXT = [
    { id: 'F01', name: 'F01 - GP - วันที่ใบสั่งซื้อ' },
    { id: 'F02', name: 'F02 - GP - ใบเสนอราคาเลขที่' },
    { id: 'F03', name: 'F03 - GP-ใบเสนอราคาลงวันที่' },
    { id: 'F04', name: 'F04 - GP-กำหนดส่งมอบภายใน(วัน)' },
    { id: 'F05', name: 'F05 - GP-ครบกำหนดส่งมอบวันที่' },
    { id: 'F06', name: 'F06 - GP-สถานที่ส่งมอบ' },
    { id: 'F07', name: 'F07 - GP-ระยะเวลารับประกัน' },
    { id: 'F08', name: 'F08 - GP-ค่าปรับรายวันอัตราร้อยละ' },
    { id: 'F09', name: 'F09 - GP-ซื้อของเป็นชุดค่าปรับวันละ' },
    { id: 'F11', name: 'F11 - GC-วันที่ทำสัญญา' },
    { id: 'F12', name: 'F12 - GC-ผู้ซื้อ/ผู้จ้าง' },
    { id: 'F13', name: 'F13 - GC-จดทะเบียนเป็นนิติบุคคล ณ' },
    { id: 'F14', name: 'F14 - GC-ผู้มีอำนาจลงนามผูกพัน' },
    { id: 'F15', name: 'F15 - GC-หนังสือรับรอง / ลงวันที่' },
    { id: 'F16', name: 'F16 - GC-เอกสารแนบท้ายสัญญา' },
    { id: 'F21', name: 'F21 - GC1,4-สถานที่ส่งมอบ' },
    { id: 'F22', name: 'F22 - GC1,4-ส่งมอบภายใน (วันที่/วัน)' },
    { id: 'F23', name: 'F23 - GC1,4-ยื่นหนังสือแจ้งกำหนดส่งณ' },
    { id: 'F24', name: 'F24 - GC1,4-ยื่นก่อนวันส่งมอบ (วัน)' },
    { id: 'F25', name: 'F25 - GC4-ออกแบบติดตั้งภายใน (วัน)' },
    { id: 'F26', name: 'F26 - GC1,4-เงินล่วงหน้าจำนวน (บาท)' },
    { id: 'F27', name: 'F27 - GC1,4-จะจ่ายให้ภายใน (วัน)' },
    { id: 'F28', name: 'F28 - GC1,4-หลักประกันเงินล่วงหน้า' },
    { id: 'F29', name: 'F29 - GC1,4-เงินที่เหลือจำนวน (บาท)' },
    { id: 'F30', name: 'F30 - GC1,4-ระยะเวลารับประกัน' },
    { id: 'F31', name: 'F31 - GC1,4-ระยะเวลาซ่อมแซมแก้ไข' },
    { id: 'F32', name: 'F32 - GC4-ขัดข้องไม่เกินเดือนละ(ชม.)' },
    { id: 'F33', name: 'F33 - GC4-หรือร้อยละ' },
    { id: 'F34', name: 'F34 - GC4-ปรับในอัตราชั่วโมงละ (บาท)' },
    { id: 'F35', name: 'F35 - GC1,4-หลักประกันสัญญา' },
    { id: 'F36', name: 'F36 - GC1-จำนวนเงินประกันสัญญา (บาท)' },
    { id: 'F37', name: 'F37 - GC1,4-เงินประกันเท่ากับร้อยละ' },
    { id: 'F38', name: 'F38 - GC1,4-บอกเลิกสัญญา/นำคอมฯกลับ' },
    { id: 'F39', name: 'F39 - GC1,4-ค่าปรับรายวันในอัตรา (%)' },
    { id: 'F40', name: 'F40 - GC4-ต้องชำระค่าปรับภายใน (วัน)' },
    { id: 'F41', name: 'F41 - GC2-หลักประกันสัญญา' },
    { id: 'F42', name: 'F42 - GC2-จำนวนเงินประกันสัญญา(บาท)' },
    { id: 'F43', name: 'F43 - GC2-งวดการจ่ายชำระเงิน' },
    { id: 'F44', name: 'F44 - GC2-เงินค่าจ้างล่วงหน้า(บาท)' },
    { id: 'F45', name: 'F45 - GC2-คืนหลักประกันเงินล่วงหน้า' },
    { id: 'F46', name: 'F46 - GC2-การหักเงินประกันผลงาน(บาท)' },
    { id: 'F47', name: 'F47 - GC2-7ก.เสนอแผนงานภายใน(วัน)' },
    { id: 'F48', name: 'F48 - GC2-เริ่มภายใน/แล้วเสร็จภายใน' },
    { id: 'F49', name: 'F49 - GC2-7ข.เริ่มงานจ้างภายในวันที่' },
    { id: 'F50', name: 'F50 - GC2-แล้วเสร็จสมบูรณ์ภายในวันที' },
    { id: 'F51', name: 'F51 - GC2-ระยะเวลารับผิดชอบความชำรุด' },
    { id: 'F52', name: 'F52 - GC2-กำหนดแก้ไขภายใน (วัน)' },
    { id: 'F53', name: 'F53 - GC2-ชำระค่าปรับวันละ(บาท)' },
    { id: 'F54', name: 'F54 - GC2-ชำระค่าควบคุมงานวันละ(บาท)' },
    { id: 'F55', name: 'F55 - GC2-ผ่านมาตรฐานฝีมือช่างจาก' },
    { id: 'F56', name: 'F56 - GC2-ฝีมือช่างในอัตราไม่ต่ำกว่า' },
    { id: 'F57', name: 'F57 - GC2-มาตรฐานฝีมือช่างสาขา' },
    { id: 'F61', name: 'F61 - GC3-วันเริ่มปฏิบัติงาน' },
    { id: 'F62', name: 'F62 - GC3-วันสิ้นสุดของสัญญา' },
    { id: 'F63', name: 'F63 - GC3-ภาคผนวก ก.' },
    { id: 'F64', name: 'F64 - GC3-ภาคผนวก ข.' },
    { id: 'F65', name: 'F65 - GC3-ภาคผนวก ค.' },
    { id: 'F66', name: 'F66 - GC5-คอมฯติดตั้งอยู่ ณ' },
    { id: 'F67', name: 'F67 - GC5-เริ่มต้นให้บริการวันที่' },
    { id: 'F68', name: 'F68 - GC5-ให้บริการถึงวันที่' },
    { id: 'F69', name: 'F69 - GC5-รวมเป็นเวลาทั้งสิ้น' },
    { id: 'F70', name: 'F70 - GC5-จำนวนงวดการจ่ายเงิน' },
    { id: 'F71', name: 'F71 - GC5-รายละเอียดงวดการจ่ายเงิน' },
    { id: 'F72', name: 'F72 - GC5-ขัดข้องไม่เกินเดือนละ(ชม.)' },
    { id: 'F73', name: 'F73 - GC5-หรือร้อยละ' },
    { id: 'F74', name: 'F74 - GC5-ปรับในอัตราชม.ละ(บาท)' },
    { id: 'F75', name: 'F75 - GC5-ช่างมาตรวจสอบเดือนละ(ครั้ง' },
    { id: 'F76', name: 'F76 - GC5-ซ่อมแซมแก้ไขภายใน' },
    { id: 'F77', name: 'F77 - GC5-ปรับรายวันในอัตราร้อยละ' },
    { id: 'F78', name: 'F78 - GC5-หลักประกันสัญญา' },
    { id: 'F79', name: 'F79 - GC5-เป็นจำนวนร้อยละ' },
    { id: 'F80', name: 'F80 - GC6-ระยะเวลาการเช่า (ปี)' },
    { id: 'F81', name: 'F81 - GC6-การชำระค่าเช่า (ลักษณะ)' },
    { id: 'F82', name: 'F82 - GC6-ส่งมอบและติดตั้งณ/ภายใน(ว)' },
    { id: 'F83', name: 'F83 - GC6-แจ้งกำหนดติดตั้งณ/ก่อน(ว)' },
    { id: 'F84', name: 'F84 - GC6-ตรวจสอบที่ติดตั้งภายใน(วัน' },
    { id: 'F85', name: 'F85 - GC6-บำรุงรักษาอย่างน้อยเดือนละ' },
    { id: 'F86', name: 'F86 - GC6-ระยะเวลาห่างไม่น้อยกว่า(ว)' },
    { id: 'F87', name: 'F87 - GC6-ขัดข้องไม่เกินเดือนละ(ชม.)' },
    { id: 'F88', name: 'F88 - GC6-หรือร้อยละ' },
    { id: 'F89', name: 'F89 - GC6-บำรุงรักษาปรับในอัตราชม.ละ' },
    { id: 'F90', name: 'F90 - GC6-ชำรุดเวลาปกติซ่อมแซมภายใน' },
    { id: 'F91', name: 'F91 - GC6-นอกเวลาปกติซ่อมแซมภายใน' },
    { id: 'F92', name: 'F92 - GC6-ซ่อมแซมปรับในอัตราชม.ละ' },
    { id: 'F93', name: 'F93 - GC6-หาเครื่องเช่าทดแทนภายใน(ว)' },
    { id: 'F94', name: 'F94 - GC6-เช่าทดแทนปรับรายวันในอัตรา' },
    { id: 'F95', name: 'F95 - GC6-หลักประกันสัญญา/จำนวนเงิน' },
    { id: 'F96', name: 'F96 - GC6-หาเครื่องทดแทนเสียหายภายใน' },
    { id: 'F97', name: 'F97 - GC6-การบอกเลิกสัญญา (เดือน)' },
    { id: 'F98', name: 'F98 - GC6-ค่าปรับรายวันอัตราร้อยละ' },
    { id: 'F99', name: 'F99 - GC6-นำคอมฯกลับคืนภายใน(วัน)' },
  ];

  public PAYMENT_BLOCK_STATUS =
    {
      DUE: '1',
      NOT_COMPLETE: '2',
      OVER_DUE: '3',
    };
}
