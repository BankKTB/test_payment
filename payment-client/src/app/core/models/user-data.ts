import { AreaData } from './area-data';

export interface UserData {
  username: string;
  ministryCode: string;
  departmentCode: string;
  companyCode: string;
  areaCode: string;
  channel: string;
  divisionCode: string;
  paymentCenterCode: string;
  costCenterId: string;
  purchasingGroupId: string;
  position: string;
  email: string;
  activeCode: string;
  status: string;
  lastLogin: string;
  webAuthorize: string;
  firstLogin: string;
  firstLoginDate: string;
  firstname: string;
  lastname: string;
  authApproval: string;
  corossattbs: string;
  idempiere: string;
  active: string;

  departmentName: string;
  companyName: string;
  divisionName: string;
  paymentCenterName: string;
  purchasingName: string;
  costCenterName: string;

  paymentCenters: [];
  area: AreaData[];
}
