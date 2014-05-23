//
//  QuestionTableViewCell.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-19.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "QuestionTableViewCell.h"

@implementation QuestionTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)setQuestion:(Question *)question{
    _question = question;
    self.textLabel.text = self.question.content;
    if(question.threads.count == 0){
        self.detailTextLabel.text = @"No Answers";
    }else if(question.threads.count == 1){
        self.detailTextLabel.text = @"1 Answer";
    }else{
        self.detailTextLabel.text = [NSString stringWithFormat:@"%d Answers", self.question.threads.count];
    }
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
