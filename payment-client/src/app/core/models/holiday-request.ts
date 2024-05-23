interface request {
  date: string;
  description: string;
  active: boolean;
}
export interface HolidayRequest {
  holidays: request[];
}
