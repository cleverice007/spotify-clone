import { useRef } from "react";
import { uploadSong } from "../services/musicService"; 
interface UploadFormProps {
  isModal?: boolean;
  onClose?: () => void;
}


function UploadForm({ isModal = false, onClose }: UploadFormProps) {
  const titleRef = useRef<HTMLInputElement | null>(null);
    const artistRef = useRef<HTMLInputElement | null>(null);
    const fileRef = useRef<HTMLInputElement | null>(null);
    const albumTitleRef = useRef<HTMLInputElement | null>(null);
    const coverRef = useRef<HTMLInputElement | null>(null);
    
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
    
      // 檢查所有的 ref.current 是否存在
      if (
        !titleRef.current ||
        !artistRef.current ||
        !fileRef.current ||
        !fileRef.current.files ||
        !albumTitleRef.current ||
        !coverRef.current ||
        !coverRef.current.files
      ) {
        alert("請確保所有字段都已填寫並選擇了文件。");
        return;
      }
            
      const songData = {
        title: titleRef.current.value,
        artist: artistRef.current.value,
        filePath: fileRef.current.files[0], 
        albumTitle: albumTitleRef.current.value,
        albumCoverUrl: coverRef.current.files[0]
      };
  
      try {
        await uploadSong(songData);
        alert("歌曲上傳成功！");
      } catch (error) {
        alert("上傳歌曲時出現錯誤。請稍後再試。");
      }
    };
  
      

  // 正常渲染表單
  const formContent = (
    <form onSubmit={handleSubmit} className="p-4 max-w-sm mx-auto">
      {/* 表單內容 */}
      <input ref={titleRef} placeholder="Title" required />
      <input ref={artistRef} placeholder="Artist" required />
      <input ref={fileRef} type="file" required />
      <input ref={albumTitleRef} placeholder="Album Title" required />
      <input ref={coverRef} type="file" required />
      <button type="submit">Upload</button>
    </form>
  );

  // 如果是模態窗口，則添加額外的樣式和關閉按鈕
  if (isModal) {
    return (
      <div className="modal bg-white shadow-lg rounded mx-auto my-8">
        {formContent}
        <button onClick={onClose}>Close</button>
      </div>
    );
  }

  return formContent;
}
export default UploadForm;