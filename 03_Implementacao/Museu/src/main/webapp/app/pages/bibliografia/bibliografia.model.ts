export interface IBibliografia {
  id?: number;
  textoPath?: string | null;
  isLivro?: boolean | null;
}

export class Bibliografia implements IBibliografia {
  constructor(public id?: number, public textoPath?: string | null, public isLivro?: boolean | null) {}
}

export function getBibliografiaIdentifier(bibliografia: IBibliografia): number | undefined {
  return bibliografia.id;
}
