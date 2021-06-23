import * as Sentry from "sentry-expo";

const log = (error) => Sentry.Native.captureException(error);
// const log2 = (error) => Sentry.Browser.captureException(error);

const start = () =>
  Sentry.init({
    dsn: sentryDSN,
    enableInExpoDevelopment: true,
    debug: true, // Sentry will try to print out useful debugging information if something goes wrong with sending an event. Set this to `false` in production.

    enableAutoSessionTracking: true,
    sessionTrackingIntervalMillis: 10000,
  });
const sentryDSN =
  "https://633626252ccc4241b608d6ccee026e94@o506078.ingest.sentry.io/5595378";
const sentryAuthToken =
  "574804e777ac4bb681cf7cfc9e01f99b1418bc7f815544cd8b8adfb1f41c5901";

export default { log, start };
