export const validArray = (array: Array, length? = 0) => {
  return array && Array.isArray(array) && array.length >= length;
};
