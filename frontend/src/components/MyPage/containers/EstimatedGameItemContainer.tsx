import React, { useState } from 'react';
import EstimatedGameItem from '../components/EstimatedGameItem';
import { EstimatedGameItemType } from '../../../typedef/Game/games.types';

const SelectGameItemContainer = () => {
	const [preference, setPreference] = useState({
		like: false,
		dislike: false,
	});
	const [selectedBefore, setSelectedBefore] = useState('');

	const handleRadioBtn = (id: string) => {
		console.log(selectedBefore, id);
		if (selectedBefore === id) {
			setSelectedBefore('');
			return false;
		}
		setSelectedBefore(id);
		return true;
	};

	const onClickPref = (e: React.MouseEvent) => {
		const id = (e.target as Element).id;
		const result = handleRadioBtn(id);

		if (id === 'like') {
			setPreference({
				...preference,
				like: result,
				dislike: result ? false : preference.dislike,
			});
		} else if (id === 'dislike') {
			setPreference({
				...preference,
				dislike: result,
				like: result ? false : preference.like,
			});
		}
	};

	const estimatedGame: EstimatedGameItemType = {
		gameSeq: 1,
		gameTitle: '젤다의 전설',
		gameTitleImageUrl: '',
		preference: preference,
		onClickPref: onClickPref,
	};

	return (
		<div>
			<EstimatedGameItem estimatedGame={estimatedGame} />
		</div>
	);
};

export default SelectGameItemContainer;
