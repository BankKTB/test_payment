import { FilterValue } from '@core/models/filter-value';

export interface Filter {
  id: string;
  value: [FilterValue];
}
