export function isExternal(path: string): boolean {
  return /^(https?:|mailto:|tel:)/.test(path)
}
export function isExternalIcon(path: string): boolean {
  return /^(\/)+([a-zA-Z0-9\s_\\.\-():/])+(.svg|.png|.jpg)$/g.test(path) || /^(https?:|mailto:|tel:)/.test(path)
}
