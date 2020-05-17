import axios from "axios";

export const notificationService = {
    getMyNotifications,
    deleteNotification
};

function getMyNotifications() {
    return axios.get("/notifications")
        .then(response => response.data);
}

function deleteNotification(id) {
    return axios.delete(`/notifications/${id}`);
}
