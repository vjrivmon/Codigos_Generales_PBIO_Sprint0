// 获取弹出框及按钮
const popup = document.getElementById('popup');
const editBtn = document.getElementById('editBtn');
const confirmBtn = document.getElementById('confirmBtn');
const cancelBtn = document.getElementById('cancelBtn');

// 点击“编辑”按钮，显示弹出框
editBtn.addEventListener('click', function() {
    popup.style.display = 'flex'; // 显示弹窗
});

// 点击“确认”按钮，继续执行操作
confirmBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // 关闭弹窗
    alert('Cambios confirmados'); // 模拟确认操作
});

// 点击“取消”按钮，关闭弹出框
cancelBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // 关闭弹窗
});
