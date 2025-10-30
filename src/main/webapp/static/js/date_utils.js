// format initiative created date with user timeZone
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.initiative-created-date').forEach(el => {
        const raw = el.textContent?.trim();
        if (!raw) return;
        const d = new Date(raw);
        if (isNaN(d)) return;
        el.textContent = d.toLocaleString('ru-RU', {
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit', second: '2-digit'
        });
    });
});