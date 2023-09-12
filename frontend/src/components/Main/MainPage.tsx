import React from 'react';

interface MainPageProps {
	visibleImages: string[];
	handlePreviousSlide: () => void;
	handleNextSlide: () => void;
}

const MainPage = ({
	visibleImages,
	handlePreviousSlide,
	handleNextSlide,
}: MainPageProps) => {
	return (
		<div className="bg-white-950 text-white-100">
			{/* 메인화면 */}
			<div className="my-20 flex max-w-[1200px] flex-col items-center gap-4 overflow-hidden">
				{/* 현재 유행 게임 */}
				<p className="h-48 flex-1">현재 유행 게임</p>
				{/* 슬라이드 */}
				<div className="relative flex items-center gap-6 overflow-hidden py-10">
					{visibleImages.map((image, index) => (
						<div
							key={index}
							className="relative flex h-72 w-72 columns-3 items-center"
						>
							<img
								className="absolute inset-0 h-full w-full object-cover object-center"
								src={image}
								alt={`게임${index + 1}`}
							/>
						</div>
					))}
				</div>

				<div className="flex items-center justify-center">
					<button
						className="bg-gray-400 mr-4 px-4 py-2 text-white-100"
						onClick={handlePreviousSlide}
					>
						이전
					</button>
					<button
						className="bg-gray-400 px-4 py-2 text-white-100"
						onClick={handleNextSlide}
					>
						다음
					</button>
				</div>
			</div>
		</div>
	);
};

export default MainPage;
