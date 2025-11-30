/**
 * Posts Management Application
 * Modern JavaScript implementation using ES6+ features
 */
class PostsApp {
    constructor() {
        this.init();
    }

    init() {
        // Event Listeners
        const btnSave = document.getElementById('btn-save');
        const btnUpdate = document.getElementById('btn-update');
        const btnDelete = document.getElementById('btn-delete');

        if (btnSave) {
            btnSave.addEventListener('click', () => this.save());
        }

        if (btnUpdate) {
            btnUpdate.addEventListener('click', () => this.update());
        }

        if (btnDelete) {
            btnDelete.addEventListener('click', () => this.delete());
        }
    }

    async save() {
        const data = {
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            content: document.getElementById('content').value
        };

        try {
            const response = await fetch('/api/v1/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            alert('글이 등록되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.error('Error saving post:', error);
            alert('글 등록 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    async update() {
        const id = document.getElementById('id').value;
        const data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        try {
            const response = await fetch(`/api/v1/posts/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            alert('글이 수정되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.error('Error updating post:', error);
            alert('글 수정 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    async delete() {
        const id = document.getElementById('id').value;

        if (!confirm('정말 삭제하시겠습니까?')) {
            return;
        }

        try {
            const response = await fetch(`/api/v1/posts/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.error('Error deleting post:', error);
            alert('글 삭제 중 오류가 발생했습니다.\n' + error.message);
        }
    }
}

// DOM이 로드되면 앱 초기화
document.addEventListener('DOMContentLoaded', () => {
    new PostsApp();
});

