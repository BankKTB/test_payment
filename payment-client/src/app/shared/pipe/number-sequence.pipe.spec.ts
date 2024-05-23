import { NumberSequencePipe } from './number-sequence.pipe';

describe('NumberSequencePipe', () => {
  it('create an instance', () => {
    const pipe = new NumberSequencePipe();
    expect(pipe).toBeTruthy();
  });
});
