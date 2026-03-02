export const environment = {
  production: true,
  API_URL: "https://bestell.ffbruckmuehl.at/api",
  WS_URL: "ws://bestell.ffbruckmuehl.at/",
  keycloak: {
    issuer: 'https://bestell.ffbruckmuehl.at/auth/',
    realm: 'bestbuam',
    clientId: 'bestbuam-frontend'
  }
};
