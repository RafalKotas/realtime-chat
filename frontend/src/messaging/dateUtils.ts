export const messageTimeCaption = (createdAt: string) => {
    const messageDate = new Date(createdAt);
    const currentDate = new Date();
    const diffMs = currentDate.getTime() - messageDate.getTime();

    const millisInMinute = 60000;
    const millisInHour = millisInMinute * 60
    const millisInDay = millisInHour * 24

    const minutes = Math.floor(millisInMinute);
    const hours = Math.floor(diffMs / (millisInHour));
    const days = Math.floor(diffMs / (millisInDay));

    if (days >= 1) {
        return days === 1 ? "yesterday" : `${days} days ago`;
    }
    if (days === 0 && hours >= 1) {
        return hours === 1 ? "1 hour ago" : `${hours} hours ago`;
    }
    if (minutes > 1) {
        return minutes === 1 ? "1 minute ago" : `${minutes} minutes ago`;
    }
    return `just now`;
}