export const validArray = (array: any, length? = 0) => {
  return array && Array.isArray(array) && array.length >= length;
};
