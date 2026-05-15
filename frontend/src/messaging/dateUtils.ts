export const messageTimeCaption = (createdAt: string) => {
    const messageDate = new Date(createdAt);
    const now = new Date();
    const diffMs = now.getTime() - messageDate.getTime();

    const minute_millis = 60_000;
    const hour_millis = minute_millis * 60;
    const day_millis = hour_millis * 24;
    const week_millis = day_millis * 7;
    const month_millis = day_millis * 30;
    const year_millis = day_millis * 365;

    if (diffMs < minute_millis) {
        return "just now";
    }

    if (diffMs < hour_millis) {
        const minutes = Math.floor(diffMs / minute_millis);
        return minutes === 1 ? "a minute ago" : `${minutes} minutes ago`;
    }

    if (diffMs < day_millis) {
        const hours = Math.floor(diffMs / hour_millis);
        return hours === 1 ? "an hour ago" : `${hours} hours ago`;
    }

    if (diffMs < week_millis) {
        const days = Math.floor(diffMs / day_millis);
        return days === 1 ? "yesterday" : `${days} days ago`;
    }

    if (diffMs < month_millis) {
        const weeks = Math.floor(diffMs / week_millis);
        return weeks === 1 ? "a week ago" : `${weeks} weeks ago`;
    }

    if (diffMs < year_millis) {
        const months = Math.floor(diffMs / month_millis);
        return months === 1 ? "a month ago" : `${months} months ago`;
    }

    const years = Math.floor(diffMs / year_millis);
    return years === 1 ? "a year ago" : `${years} years ago`;
};