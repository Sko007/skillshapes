export interface ISkillshapes {
  User: User;
}

export interface User {
  FirstName: string;
  LastName: string;
  Email: string;
  Skillshapes: Skillshape[];
}

export interface Skillshape {
  Frontend?: Frontend[];
  Backend?: Backend[];
}

export interface Frontend {
  Git?: number;
  React?: number;
  Vue?: number;
  Angular?: number;
}

export interface Backend {
  Node?: number;
  Java?: number;
}
